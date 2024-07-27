package praca_inz_proj;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class AppRestController {

	private static final Logger logger = LogManager.getLogger(AppRestController.class);

	@Autowired
	private PrzedmiotyDAO daoPrzedmioty;

	@Autowired
	private StudenciDAO daoStudenci;

	@Autowired
	private ProwadzacyDAO daoProwadzacy;

	@Autowired
	private KonsultacjeDAO daoKonsultacje;

	@Autowired
	private ForaDAO daoFora;

	@Autowired
	private SpecjalizacjeDAO daoSpecjalizacje;

	@Autowired
	private RealizacjeDAO daoRealizacje;

	@Autowired
	private WiadomosciDAO daoWiadomosci;
	
	@Autowired
	private StudentKonsultacjaDAO daoStudentKonsultacja;

	@GetMapping("/przedmioty/json")
	@ResponseBody
	public List<Przedmiot> viewPrzedmiotyJson() {
		return daoPrzedmioty.list();
	}

	@PostMapping("/przedmioty/save")
	public Przedmiot save(@RequestBody Przedmiot przedmiot) {
		logger.info("Received request to save przedmiot: {}", przedmiot);
		Przedmiot savedPrzedmiot = daoPrzedmioty.savePrzedmiot(przedmiot);
		logger.info("Saved przedmiot: {}", savedPrzedmiot);
		return savedPrzedmiot;
	}

	@GetMapping("/prowadzacy/json")
	@ResponseBody
	public List<Prowadzacy> viewProwadzacyJson() {
		List<Prowadzacy> listProwadzacy = daoProwadzacy.list();
		for (Prowadzacy prowadzacy : listProwadzacy) {
			String prowadzacyImageBase64 = convertBlobToBase64(prowadzacy.getZdjecie());
			prowadzacy.setZdjecie64(prowadzacyImageBase64);
			prowadzacy.setZdjecie(null);
		}

		return listProwadzacy;
	}

	@PostMapping("/login/mobile")
	public ResponseEntity<LoginResponse> loginMobile(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String password = request.get("password");

		Student student = daoStudenci.getStudentByEmail(email);
		Prowadzacy prowadzacy = daoProwadzacy.getProwadzacyByEmail(email);

		if (student == null && prowadzacy == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		} else if (prowadzacy != null && !prowadzacy.getHaslo().equals(password)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		} else if (student != null && !String.valueOf(student.getNr_indeksu()).equals(password)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		} else {

			if (student != null) {
				String studentImageBase64 = convertBlobToBase64(student.getZdjecie());
				student.setZdjecie64(studentImageBase64);
				student.setZdjecie(null);
			}

			if (prowadzacy != null) {
				String prowadzacyImageBase64 = convertBlobToBase64(prowadzacy.getZdjecie());
				prowadzacy.setZdjecie64(prowadzacyImageBase64);
				prowadzacy.setZdjecie(null);
			}
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setStudent(student);
			loginResponse.setProwadzacy(prowadzacy);

			try {
				ObjectMapper objectMapper = new ObjectMapper();
				String jsonResponse = objectMapper.writeValueAsString(loginResponse);
				System.out.println("JSON response: " + jsonResponse);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			return ResponseEntity.ok(loginResponse);
		}
	}

	private String convertBlobToBase64(Blob blob) {
		if (blob == null) {
			return null;
		}

		try (InputStream inputStream = blob.getBinaryStream()) {
			byte[] bytes = inputStream.readAllBytes();
			String base64Image = Base64.getEncoder().encodeToString(bytes);
			return base64Image;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	@GetMapping("/konsultacje/mobileStudent/{nrStudenta}")
	@ResponseBody
	public List<Konsultacja> getKonsultacjeByNrStudenta(@PathVariable int nrStudenta) {
		List<Konsultacja> konsultacje = daoKonsultacje.getKonsultacjeByNrStudenta(nrStudenta);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	    for (Konsultacja konsultacja : konsultacje) {
	        java.util.Date utilDataOd = new java.util.Date(konsultacja.getDataOd().getTime());
	        java.util.Date utilDataDo = new java.util.Date(konsultacja.getDataDo().getTime());

	        LocalDateTime dataOd = LocalDateTime.ofInstant(utilDataOd.toInstant(), ZoneId.systemDefault());
	        LocalDateTime dataDo = LocalDateTime.ofInstant(utilDataDo.toInstant(), ZoneId.systemDefault());

	        String formattedDataOd = dataOd.format(formatter);
	        String formattedDataDo = dataDo.format(formatter);

	        konsultacja.setFormattedDataOd(formattedDataOd);
	        konsultacja.setFormattedDataDo(formattedDataDo);
	    }
	    
		System.out.println("konsultacje" + konsultacje);
		return konsultacje;
	}

	@GetMapping("/konsultacje/mobileProwadzacy/{nrProwadzacego}")
	@ResponseBody
	public List<Konsultacja> getKonsultacjeByNrProwadzacego(@PathVariable int nrProwadzacego) {
		List<Konsultacja> konsultacje = daoKonsultacje.getKonsultacjeByNrProwadzacego(nrProwadzacego);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	    for (Konsultacja konsultacja : konsultacje) {
	        java.util.Date utilDataOd = new java.util.Date(konsultacja.getDataOd().getTime());
	        java.util.Date utilDataDo = new java.util.Date(konsultacja.getDataDo().getTime());

	        LocalDateTime dataOd = LocalDateTime.ofInstant(utilDataOd.toInstant(), ZoneId.systemDefault());
	        LocalDateTime dataDo = LocalDateTime.ofInstant(utilDataDo.toInstant(), ZoneId.systemDefault());

	        String formattedDataOd = dataOd.format(formatter);
	        String formattedDataDo = dataDo.format(formatter);

	        konsultacja.setFormattedDataOd(formattedDataOd);
	        konsultacja.setFormattedDataDo(formattedDataDo);
	    }
	    
		System.out.println("konsultacje" + konsultacje);
		return konsultacje;
	}



	@PostMapping("/konsultacja/save")
	public Konsultacja save(@RequestBody Konsultacja konsultacja) {
		logger.info("Received request to save konsultacja: {}", konsultacja);
		Konsultacja savedKonsultacja = daoKonsultacje.saveKonsultacja(konsultacja);
		logger.info("Saved konsultacja: {}", savedKonsultacja);
		return savedKonsultacja;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@PostMapping("konsultacja/mobile/update")
	public ResponseEntity<Void> updateKonsultacja(@RequestBody Konsultacja konsultacja) {
	    Konsultacja originalKonsultacja = daoKonsultacje.get(konsultacja.getNr_konsultacji());
	    daoKonsultacje.update(konsultacja);

	    boolean hasChanges = !originalKonsultacja.getDataOd().equals(konsultacja.getDataOd())
	            || !originalKonsultacja.getDataDo().equals(konsultacja.getDataDo())
	            || !Objects.equals(originalKonsultacja.getNr_pokoju(), konsultacja.getNr_pokoju())
	            || !originalKonsultacja.getCzy_publiczne().equals(konsultacja.getCzy_publiczne())
	            || !originalKonsultacja.getCzy_online().equals(konsultacja.getCzy_online());
	    if(hasChanges) {
		StringBuilder notificationMessage = new StringBuilder("Zmodyfikowano konsultacje:\n");
		notificationMessage.append("Poprzednie dane:\n");

		LocalDateTime originalDateTimeOd = LocalDateTime.ofInstant(originalKonsultacja.getDataOd().toInstant(),
				ZoneId.systemDefault());
		LocalDateTime originalDateTimeDo = LocalDateTime.ofInstant(originalKonsultacja.getDataDo().toInstant(),
				ZoneId.systemDefault());

		if (!originalDateTimeOd.equals(konsultacja.getDataOd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
			String formattedOriginalDataOd = originalDateTimeOd
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			String formattedDataOd = konsultacja.getDataOd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			notificationMessage.append("Data od: ").append(formattedOriginalDataOd).append(" (zmienione na: ")
					.append(formattedDataOd).append(")\n");
		}

		if (!originalDateTimeDo.equals(konsultacja.getDataDo().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
			String formattedOriginalDataDo = originalDateTimeDo
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			String formattedDataDo = konsultacja.getDataDo().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			notificationMessage.append("Data do: ").append(formattedOriginalDataDo).append(" (zmienione na: ")
					.append(formattedDataDo).append(")\n");
		}

		if (!originalKonsultacja.getNr_pokoju().equals(String.valueOf(konsultacja.getNr_pokoju()))) {
			notificationMessage.append("Nr pokoju: ").append(originalKonsultacja.getNr_pokoju()).append(" (zmienione na: ")
					.append(konsultacja.getNr_pokoju()).append(")\n");
		}
		if (!originalKonsultacja.getCzy_publiczne().equals(konsultacja.getCzy_publiczne())) {
			notificationMessage.append("Czy publiczne: ").append(originalKonsultacja.getFullPubliczne()).append(" (zmienione na: ")
					.append(konsultacja.getFullPubliczne()).append(")\n");
		}
		if (!originalKonsultacja.getCzy_online().equals(konsultacja.getFullOnline())) {
			notificationMessage.append("Czy online: ").append(originalKonsultacja.getFullOnline()).append(" (zmienione na: ")
					.append(konsultacja.getFullOnline()).append(")\n");
		}

		List<StudentKonsultacja> studentKonsultacjaList = daoStudentKonsultacja
				.getStudentKonsultacjaByNrKonsultacji(konsultacja.getNr_konsultacji());
		if (studentKonsultacjaList != null) {
			for (StudentKonsultacja studentKonsultacja : studentKonsultacjaList) {
				studentKonsultacja.setEdycja(notificationMessage.toString());
				daoStudentKonsultacja.update(studentKonsultacja);
			}
		}
	    }

	    return ResponseEntity.ok().build();
	}

	
	@GetMapping("/konsultacje/mobile")
	@ResponseBody
	public List<Konsultacja> viewKonsultacjeJson() {
		List<Konsultacja> konsultacje = daoKonsultacje.list();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	    for (Konsultacja konsultacja : konsultacje) {
	        java.util.Date utilDataOd = new java.util.Date(konsultacja.getDataOd().getTime());
	        java.util.Date utilDataDo = new java.util.Date(konsultacja.getDataDo().getTime());

	        LocalDateTime dataOd = LocalDateTime.ofInstant(utilDataOd.toInstant(), ZoneId.systemDefault());
	        LocalDateTime dataDo = LocalDateTime.ofInstant(utilDataDo.toInstant(), ZoneId.systemDefault());

	        String formattedDataOd = dataOd.format(formatter);
	        String formattedDataDo = dataDo.format(formatter);

	        konsultacja.setFormattedDataOd(formattedDataOd);
	        konsultacja.setFormattedDataDo(formattedDataDo);
	    }
	    
		System.out.println("konsultacje" + konsultacje);
		return konsultacje;
	}
	
	@PostMapping("/konsultacje/signup")
	public ResponseEntity<String> signUpForKonsultacja(@RequestParam int nr_konsultacji, @RequestParam int nr_studenta) {   
	    try {
	        daoStudentKonsultacja.signUp(nr_studenta, nr_konsultacji);
	        return ResponseEntity.ok("Zapisano studenta na konsultacje");
	    } catch (Exception e) {
	        System.out.println("Zapis studenta nie uda³ siê");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("B³¹d zapisu studenta");
	    }
	}


	@GetMapping("/konsultacje/checkSignUp")
	public ResponseEntity<Boolean> checkStudentKonsultacja(@RequestParam int nr_konsultacji, @RequestParam int nr_studenta) {
	    boolean isSignedUp = daoStudentKonsultacja.checkStudentKonsultacja(nr_studenta, nr_konsultacji);
	    return ResponseEntity.ok(isSignedUp);
	}
	
	@PostMapping("/konsultacje/mobile/signOut")
	public ResponseEntity<Void> signOutFromKonsultacja(
	        @RequestParam int nr_konsultacji,
	        @RequestParam int nr_studenta) {
	    if (daoStudentKonsultacja.checkStudentKonsultacja(nr_studenta, nr_konsultacji)) {
	        daoStudentKonsultacja.deleteByNrStudentaAndNrKonsultacji(nr_studenta, nr_konsultacji);
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@PutMapping("konsultacje/mobile/updatePytanie/{nr_konsultacji}")
	public ResponseEntity<Void> updatePytanie(@PathVariable int nr_konsultacji, @RequestParam String pytanie) {
	    daoKonsultacje.editPytanie(nr_konsultacji, pytanie);
	    return ResponseEntity.noContent().build();
	}

	@GetMapping("/fora/mobile")
	public List<Forum> getAllFora(HttpSession session) {
		List<Forum> fora = daoFora.list();
		Collections.reverse(fora);
		return fora;
	}

	@GetMapping("/specjalizacje/{nrSpecjalizacji}")
	public ResponseEntity<Specjalizacja> getSpecjalizacjaData(@PathVariable int nrSpecjalizacji) {
		Specjalizacja specjalizacja = daoSpecjalizacje.get(nrSpecjalizacji);
		if (specjalizacja != null) {
			return new ResponseEntity<>(specjalizacja, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/realizacje/{nrRealizacji}")
	public ResponseEntity<Realizacja> getRealizacjaData(@PathVariable int nrRealizacji) {
		Realizacja realizacja = daoRealizacje.get(nrRealizacji);
		if (realizacja != null) {
			return new ResponseEntity<>(realizacja, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/przedmioty/{nrPrzedmiotu}")
	public ResponseEntity<Przedmiot> getPrzedmiotData(@PathVariable int nrPrzedmiotu) {
		Przedmiot przedmiot = daoPrzedmioty.get(nrPrzedmiotu);
		if (przedmiot != null) {
			return new ResponseEntity<>(przedmiot, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/forums/{forumId}/messages")
	public List<Wiadomosc> getMessagesForForum(@PathVariable int forumId) {
		return daoWiadomosci.getMessages(forumId);
	}

	@PostMapping("/forums/{forumId}/messages")
	public ResponseEntity<Void> addNewMessage(@PathVariable int forumId, @RequestParam String nadawca,
			@RequestParam String messageText, @RequestParam Timestamp timestamp, @RequestParam Integer nrStudenta,
			@RequestParam Integer nrProwadzacego) {
		Wiadomosc wiadomosc = new Wiadomosc();
		wiadomosc.setNadawca(nadawca);
		wiadomosc.setTekst(messageText);
		wiadomosc.setCzas(timestamp);
		daoWiadomosci.addMessage(wiadomosc, forumId, nrStudenta, nrProwadzacego);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/student/{nrStudenta}/image")
	public ResponseEntity<String> getStudentImage(@PathVariable int nrStudenta) {
		Student student = daoStudenci.get(nrStudenta);
		if (student == null) {
			return ResponseEntity.notFound().build();
		}

		String imageData;
		String defaultImageFilename;
		if (student.getZdjecie() != null) {
			imageData = convertBlobToBase64(student.getZdjecie());
		} else {
			if (student.getPlec().equals("M")) {
				defaultImageFilename = "default_man.png";
			} else {
				defaultImageFilename = "default_woman.png";
			}

			try {
				Resource resource = new ClassPathResource("static/images/" + defaultImageFilename);
				byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
				imageData = Base64.getEncoder().encodeToString(imageBytes);
			} catch (IOException e) {
				imageData = "";
			}
		}
		return ResponseEntity.ok(imageData);
	}

	@GetMapping("/prowadzacy/{nrProwadzacego}/image")
	public ResponseEntity<String> getProwadzacyImage(@PathVariable int nrProwadzacego) {
		Prowadzacy prowadzacy = daoProwadzacy.get(nrProwadzacego);
		if (prowadzacy == null) {
			return ResponseEntity.notFound().build();
		}

		String imageData;
		String defaultImageFilename;
		if (prowadzacy.getZdjecie() != null) {
			imageData = convertBlobToBase64(prowadzacy.getZdjecie());
		} else {
			if (prowadzacy.getPlec().equals("M")) {
				defaultImageFilename = "default_man.png";
			} else {
				defaultImageFilename = "default_woman.png";
			}

			try {
				Resource resource = new ClassPathResource("static/images/" + defaultImageFilename);
				byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
				imageData = Base64.getEncoder().encodeToString(imageBytes);
			} catch (IOException e) {
				imageData = "";
			}
		}
		return ResponseEntity.ok(imageData);
	}

	@PostMapping("/update_student_data")
	public ResponseEntity<String> updateStudentData(@RequestParam int nr_studenta,
			@RequestBody Map<String, String> data) {
		String email = data.get("email");
		String nrTel = data.get("nrTel");

		System.out.println("Received request: nr studenta=" + nr_studenta + ", email=" + email + ", nrTel=" + nrTel);
		try {
			daoStudenci.updateStudentEmailAndNrTel(nr_studenta, email, nrTel);
			return ResponseEntity.ok("Student data updated successfully!");
		} catch (Exception e) {
			System.out.println("Coœ posz³o nie tak");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating student data.");
		}
	}
	
	@PostMapping("/update_prowadzacy_data")
	public ResponseEntity<String> updateProwadzacyData(@RequestParam int nr_prowadzacego,
			@RequestBody Map<String, String> data) {
		String email = data.get("email");
		String nrTel = data.get("nrTel");

		System.out.println("Received request: nr prowadzacego=" + nr_prowadzacego + ", email=" + email + ", nrTel=" + nrTel);
		try {
			daoProwadzacy.updateProwadzacyEmailAndNrTel(nr_prowadzacego, email, nrTel);
			return ResponseEntity.ok("Prowadzacy data updated successfully!");
		} catch (Exception e) {
			System.out.println("Coœ posz³o nie tak");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating student data.");
		}
	}

	@GetMapping("/prowadzacy/mobile/{nrProwadzacego}")
	@ResponseBody
	public Prowadzacy getProwadzacyById(@PathVariable int nrProwadzacego) {
		Prowadzacy prowadzacy = daoProwadzacy.get(nrProwadzacego);
		if (prowadzacy != null) {
			String imageData;
			String defaultImageFilename;

			if (prowadzacy.getZdjecie() != null) {
				imageData = convertBlobToBase64(prowadzacy.getZdjecie());
			} else {
				if (prowadzacy.getPlec().equals("M")) {
					defaultImageFilename = "default_man.png";
				} else {
					defaultImageFilename = "default_woman.png";
				}

				try {
					Resource resource = new ClassPathResource("static/images/" + defaultImageFilename);
					byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
					imageData = Base64.getEncoder().encodeToString(imageBytes);
				} catch (IOException e) {
					imageData = "";
				}
			}
			prowadzacy.setZdjecie(null);
			prowadzacy.setZdjecie64(imageData);
		}
		return prowadzacy;

	}
}
