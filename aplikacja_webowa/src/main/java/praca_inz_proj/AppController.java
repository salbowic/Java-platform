package praca_inz_proj;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

	@Autowired
	private PrzedmiotyDAO daoPrzedmioty;

	@Autowired
	private StudenciDAO daoStudenci;

	@Autowired
	private ProwadzacyDAO daoProwadzacy;

	@Autowired
	private RealizacjeDAO daoRealizacje;

	@Autowired
	private ForaDAO daoFora;

	@Autowired
	private KonsultacjeDAO daoKonsultacje;

	@Autowired
	private StudentKonsultacjaDAO daoStudentKonsultacja;

	@Autowired
	private WiadomosciDAO WiadomosciDAO;

	public boolean checkIfLoggedIn(HttpSession session, Model model) {
		if (session.getAttribute("loggedInUser") == null) {
			model.addAttribute("error", "Musisz byæ zalogowany");
			return false;
		}
		return true;
	}

	@GetMapping("/")
	public String home(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Object loggedInUser = session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			// User is not logged in, redirect to login page
			return "redirect:/login";
		} else if (loggedInUser instanceof Student) {
			model.addAttribute("student", (Student) loggedInUser);
			return "redirect:/profilStudent";
		} else if (loggedInUser instanceof Prowadzacy) {
			model.addAttribute("prowadzacy", (Prowadzacy) loggedInUser);
			return "redirect:/profilProwadzacy";
		} else {
			return "error";
		}
	}

	@GetMapping("/login")
	public String viewLoginPage() {
		return "login";
	}

	/* LOGIN, PROFILE */
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model,
			HttpServletResponse response) {
		Student student = daoStudenci.getStudentByEmail(email);
		Prowadzacy prowadzacy = daoProwadzacy.getProwadzacyByEmail(email);

		if (student == null && prowadzacy == null) {
			model.addAttribute("error", "Niepoprawny email lub has³o");
			return "login";
		} else if (prowadzacy != null && !prowadzacy.getHaslo().equals(password)) {
			model.addAttribute("error", "Niepoprawny email lub has³o");
			return "login";
		} else if (student != null && !String.valueOf(student.getNr_indeksu()).equals(password)) {
			model.addAttribute("error", "Niepoprawny email lub has³o");
			return "login";
		} else {
			Object loggedInUser = student != null ? student : prowadzacy;
			session.setAttribute("loggedInUser", loggedInUser);

			String nadawca = "";
			if (loggedInUser instanceof Student) {
				Student studentNadawca = (Student) loggedInUser;
				nadawca = "Stud. " + studentNadawca.getImie() + " " + studentNadawca.getNazwisko();
			} else if (loggedInUser instanceof Prowadzacy) {
				Prowadzacy prowadzacyNadawca = (Prowadzacy) loggedInUser;
				nadawca = prowadzacyNadawca.getFullTytul() + " " + prowadzacyNadawca.getImie() + " "
						+ prowadzacy.getNazwisko();
			}
			session.setAttribute("nadawca", nadawca);

			try {
				if (prowadzacy != null) {
					response.sendRedirect("profilProwadzacy");
					model.addAttribute("nr_prowadzacego", ((Prowadzacy) loggedInUser).getNr_prowadzacego());
				} else {
					response.sendRedirect("profilStudent");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@GetMapping("/profilStudent")
	public String profilStudent(HttpSession session, Model model) {
		Student loggedInStudent = (Student) session.getAttribute("loggedInUser");
		if (loggedInStudent == null) {
			model.addAttribute("error", "Musisz byæ zalogowany");
			return "redirect:/login";
		}

		Student student = daoStudenci.getStudentByEmail(loggedInStudent.getEmail());
		model.addAttribute("student", student);

		String base64Image = convertBlobToBase64(student.getZdjecie());
		model.addAttribute("studentImage", base64Image);

		List<StudentKonsultacja> studentNotifications = daoStudentKonsultacja
				.getStudentKonsultacjaByNrStudenta(student.getNr_studenta());
		model.addAttribute("studentNotifications", studentNotifications);

		return "studentDashboard";
	}

	@GetMapping("/profilProwadzacy")
	public String profilProwadzacy(HttpSession session, Model model) {
		Prowadzacy loggedInProwadzacy = (Prowadzacy) session.getAttribute("loggedInUser");
		if (loggedInProwadzacy == null) {
			model.addAttribute("error", "Musisz byæ zalogowany");
			return "redirect:/login";
		}
		Prowadzacy prowadzacy = daoProwadzacy.getProwadzacyByEmail(loggedInProwadzacy.getEmail());
		model.addAttribute("prowadzacy", prowadzacy);

		String base64Image = convertBlobToBase64(prowadzacy.getZdjecie());
		model.addAttribute("prowadzacyImage", base64Image);

		List<Konsultacja> konsultacje = daoKonsultacje.getKonsultacjeByNrProwadzacego(prowadzacy.getNr_prowadzacego());

		model.addAttribute("konsultacje", konsultacje);

		return "prowadzacyDashboard";
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

	@PostMapping("/deleteNotification")
	public String deleteNotification(@RequestParam("nrKonsultacji") int nr_konsultacji, HttpSession session) {

		Object loggedInUser = session.getAttribute("loggedInUser");

		if (loggedInUser instanceof Student) {
			daoStudentKonsultacja.deleteEdycjaByNrStudentaAndNrKonsultacji(((Student) loggedInUser).getNr_studenta(),
					nr_konsultacji);
			return "redirect:/profilStudent";
		} else if (loggedInUser instanceof Prowadzacy) {
			daoKonsultacje.deletePowiadomienie(nr_konsultacji);
			return "redirect:/profilProwadzacy";
		}

		return "redirect:/login";
	}

	/* PRZEDMIOTY */
	@GetMapping("/przedmioty")
	public String viewPrzedmiotyPage(Model model, HttpSession session) {
		if (checkIfLoggedIn(session, model)) {
			List<Przedmiot> listPrzedmiot = daoPrzedmioty.list();
			model.addAttribute("listPrzedmiot", listPrzedmiot);
			return "przedmioty";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/searchPrzedmioty")
	public String searchPrzedmioty(@RequestParam("nazwaPrzedmiotu") String nazwaPrzedmiotu, Model model) {
		model.addAttribute("nazwaPrzedmiotu", nazwaPrzedmiotu); // Add this line
		return "redirect:/filteredPrzedmioty?nazwaPrzedmiotu=" + nazwaPrzedmiotu;
	}

	@GetMapping("/filteredPrzedmioty")
	public String filteredPrzedmiotyPage(
			@RequestParam(value = "nazwaPrzedmiotu", required = false) String nazwaPrzedmiotu, Model model) {
		List<Przedmiot> matchingPrzedmioty;
		if (nazwaPrzedmiotu != null) {
			matchingPrzedmioty = daoPrzedmioty.findByNazwaPrzedmiotuContainingIgnoreCase(nazwaPrzedmiotu);
		} else {
			matchingPrzedmioty = daoPrzedmioty.list();
		}
		model.addAttribute("listPrzedmiot", matchingPrzedmioty);
		model.addAttribute("nazwaPrzedmiotu", nazwaPrzedmiotu);
		return "filteredPrzedmioty";
	}

	/* PROWADZACY */
	@GetMapping("/prowadzacy")
	public String viewProwadzacyPage(Model model, HttpSession session) {
		if (checkIfLoggedIn(session, model)) {

			List<Prowadzacy> listProwadzacy = daoProwadzacy.list();
			for (Prowadzacy prowadzacy : listProwadzacy) {
				Blob zdjecie = prowadzacy.getZdjecie();
				String base64Image = convertBlobToBase64(zdjecie);
				prowadzacy.setZdjecie64(base64Image);
			}
			model.addAttribute("listProwadzacy", listProwadzacy);
			return "prowadzacy";

		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/searchProwadzacy")
	public String searchProwadzacy(@RequestParam("keyword") String keyword, Model model) {
		model.addAttribute("keyword", keyword);
		return "redirect:/filteredProwadzacy?keyword=" + keyword;
	}

	@GetMapping("/filteredProwadzacy")
	public String filteredProwadzacyPage(@RequestParam(value = "keyword", required = false) String keyword,
			Model model) {
		List<Prowadzacy> matchingProwadzacy;
		if (keyword != null) {
			matchingProwadzacy = daoProwadzacy.findByImieAndNazwisko(keyword);
		} else {
			matchingProwadzacy = daoProwadzacy.list();
		}
		for (Prowadzacy prowadzacy : matchingProwadzacy) {
			Blob zdjecie = prowadzacy.getZdjecie();
			String base64Image = convertBlobToBase64(zdjecie);
			prowadzacy.setZdjecie64(base64Image);
		}
		model.addAttribute("listProwadzacy", matchingProwadzacy);
		model.addAttribute("keyword", keyword);
		return "filteredProwadzacy";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}

	/* EDIT */
	@RequestMapping("/editStudent/{student.Email}")
	public ModelAndView viewEditStudentProfilPage(HttpSession session, Model model) {
		ModelAndView mav = new ModelAndView();
		Student loggedInStudent = (Student) session.getAttribute("loggedInUser");
		if (loggedInStudent == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		mav.setViewName("editStudent");
		Student student = daoStudenci.getStudentByEmail(loggedInStudent.getEmail());
		mav.addObject("student", student);
		return mav;

	}

	@RequestMapping("/editProwadzacy/{prowadzacy.Email}")
	public ModelAndView viewEditProwadzacyProfilPage(HttpSession session, Model model) {
		ModelAndView mav = new ModelAndView();
		Prowadzacy loggedInProwadzacy = (Prowadzacy) session.getAttribute("loggedInUser");
		if (loggedInProwadzacy == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		mav.setViewName("editProwadzacy");
		Prowadzacy prowadzacy = daoProwadzacy.getProwadzacyByEmail(loggedInProwadzacy.getEmail());
		mav.addObject("prowadzacy", prowadzacy);
		return mav;
	}

	/* UPDATE */
	@RequestMapping(value = "/updateStudent", method = RequestMethod.POST)
	public String updateStudent(@ModelAttribute("student") Student student,
			@RequestParam("imageFile") MultipartFile imageFile,
			@RequestParam(value = "deleteImage", required = false) String deleteImage, HttpSession session,
			Model model) {

		if (checkIfLoggedIn(session, model)) {

			Student loggedInStudent = (Student) session.getAttribute("loggedInUser");
			loggedInStudent.setEmail(student.getEmail());
			loggedInStudent.setNr_telefonu(student.getNr_telefonu());

			if (deleteImage != null) {
				loggedInStudent.setZdjecie(null);

			} else if (!imageFile.isEmpty()) {
				try {
					byte[] imageData = imageFile.getBytes();
					Blob imageBlob = new SerialBlob(imageData);

					loggedInStudent.setZdjecie(imageBlob);
				} catch (IOException | SQLException e) {
					e.printStackTrace();
					model.addAttribute("error", "Failed to process the image file.");
				}
			}

			System.out.println("Updating student: " + loggedInStudent);

			daoStudenci.update(loggedInStudent);

			return "redirect:/profilStudent";
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/updateProwadzacy", method = RequestMethod.POST)
	public String updateProwadzacy(@ModelAttribute("prowadzacy") Prowadzacy prowadzacy,
			@RequestParam("imageFile") MultipartFile imageFile,
			@RequestParam(value = "deleteImage", required = false) String deleteImage, HttpSession session,
			Model model) {

		if (checkIfLoggedIn(session, model)) {
			Prowadzacy loggedInProwadzacy = (Prowadzacy) session.getAttribute("loggedInUser");
			loggedInProwadzacy.setEmail(prowadzacy.getEmail());
			loggedInProwadzacy.setNr_telefonu(prowadzacy.getNr_telefonu());

			if (deleteImage != null) {
				loggedInProwadzacy.setZdjecie(null);

			} else if (!imageFile.isEmpty()) {
				try {
					byte[] imageData = imageFile.getBytes();
					Blob imageBlob = new SerialBlob(imageData);
					loggedInProwadzacy.setZdjecie(imageBlob);
				} catch (IOException | SQLException e) {
					e.printStackTrace();
					model.addAttribute("error", "Failed to process the image file.");
				}
			}

			System.out.println("Updating prowadzacy: " + loggedInProwadzacy);
			daoProwadzacy.update(loggedInProwadzacy);

			return "redirect:/profilProwadzacy";
		} else {
			return "redirect:/login";
		}
	}

	/* ADD */
	@RequestMapping(value = "/newKonsultacja", method = RequestMethod.POST)
	public String saveKonsultacja(@ModelAttribute("konsultacja") Konsultacja konsultacja,
			@RequestParam("dataOd") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date dataOd,
			@RequestParam("dataDo") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date dataDo, HttpSession session,
			Model model) {

		if (checkIfLoggedIn(session, model)) {
			konsultacja.setDataOd(dataOd);
			konsultacja.setDataDo(dataDo);

			Object loggedInUser = session.getAttribute("loggedInUser");
			if (loggedInUser instanceof Prowadzacy) {
				konsultacja.setNr_prowadzacego(((Prowadzacy) loggedInUser).getNr_prowadzacego());
			}

			daoKonsultacje.save(konsultacja);

			return "redirect:/konsultacje";
		}

		return "redirect:/login";
	}

	/* KONSULTACJE */
	@GetMapping("/konsultacje")
	public String konsultacje(HttpSession session, HttpServletResponse response, Model model) {
		if (checkIfLoggedIn(session, model)) {

			Object loggedInUser = session.getAttribute("loggedInUser");
			if (loggedInUser instanceof Student) {
				return "redirect:/konsultacjeStudent";
			} else if (loggedInUser instanceof Prowadzacy) {
				return "redirect:/konsultacjeProwadzacy";
			} else {
				return "redirect:/login";
			}

		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/konsultacjeStudent")
	public String viewKonsultacjeStudentPage(Model model, HttpSession session) {
		Object loggedInUser = session.getAttribute("loggedInUser");
		if (loggedInUser instanceof Student) {
			int nrStudenta = ((Student) loggedInUser).getNr_studenta();
			List<Konsultacja> konsultacje = daoKonsultacje.getKonsultacjeByNrStudenta(nrStudenta);
			if (konsultacje.isEmpty()) {
				model.addAttribute("message", "Brak konsultacji dla tego studenta.");
			} else {
				model.addAttribute("listKonsultacjaS", konsultacje);
				model.addAttribute("daoProwadzacy", daoProwadzacy);
				model.addAttribute("daoPrzedmioty", daoPrzedmioty);
			}
			return "konsultacjeS";
		} else {
			return "redirect:/login";
		}
	}

	@PostMapping("/EditPytanie")
	public String editPytanie(@RequestParam("konsultacjaId") int konsultacjaId,
			@RequestParam("pytanie") String pytanie) {
		Konsultacja konsultacja = daoKonsultacje.get(konsultacjaId);
		System.out.println(pytanie);
		konsultacja.setPytanie(pytanie);
		String powiadomienie = "ID konsultacji: " + konsultacja.getNr_konsultacji() + "|||" + "Data: "
				+ konsultacja.getDataOd() + "|||" + "Pytanie: " + "|||" + pytanie;
		System.out.println(powiadomienie);
		konsultacja.setPowiadomienie(powiadomienie);
		daoKonsultacje.update(konsultacja);

		return "redirect:/konsultacjeStudent";
	}

	@GetMapping("/konsultacjeProwadzacy")
	public String viewKonsultacjeProwadzacyPage(Model model, HttpSession session) {
		Object loggedInUser = session.getAttribute("loggedInUser");
		if (loggedInUser instanceof Prowadzacy) {
			int nrProwadzacego = ((Prowadzacy) loggedInUser).getNr_prowadzacego();
			List<Konsultacja> konsultacje = daoKonsultacje.getKonsultacjeByNrProwadzacego(nrProwadzacego);
			if (konsultacje.isEmpty()) {
				model.addAttribute("message", "Brak konsultacji dla tego prowadz¹cego.");
			} else {
				for (Konsultacja konsultacja : konsultacje) {
					int nrKonsultacji = konsultacja.getNr_konsultacji();
					List<Student> studenci = daoStudenci.getStudentsByNrKonsultacji(nrKonsultacji);
					konsultacja.setZapisaniStudenci(studenci);
					for (Student student : studenci) {
						Blob zdjecie = student.getZdjecie();
						String base64Image = convertBlobToBase64(zdjecie);
						student.setZdjecie64(base64Image);
					}
				}
				model.addAttribute("listKonsultacjaP", konsultacje);
				model.addAttribute("daoPrzedmioty", daoPrzedmioty);
				model.addAttribute("daoStudenci", daoStudenci);
			}
			return "konsultacjeP";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/DodajKonsultacje")
	public String showAddKonsultacja(HttpSession session, Model model) {
		if (checkIfLoggedIn(session, model)) {
			List<Realizacja> listRealizacja = daoRealizacje.list();
			model.addAttribute("listRealizacja", listRealizacja);
			model.addAttribute("przedmiotyDAO", daoPrzedmioty);
			Konsultacja konsultacja = new Konsultacja();
			model.addAttribute("konsultacja", konsultacja);

			return "AddKonsultacja";

		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/EditKonsultacja")
	public String showEditKonsultacja(@RequestParam("nr_konsultacji") int nrKonsultacji, HttpSession session,
			Model model) {
		if (checkIfLoggedIn(session, model)) {
			Konsultacja konsultacja = daoKonsultacje.get(nrKonsultacji);

			if (konsultacja != null) {
				List<Realizacja> listRealizacja = daoRealizacje.list();
				model.addAttribute("listRealizacja", listRealizacja);
				model.addAttribute("przedmiotyDAO", daoPrzedmioty);

				LocalDateTime dataOd = LocalDateTime.ofInstant(konsultacja.getDataOd().toInstant(),
						ZoneId.systemDefault());
				LocalDateTime dataDo = LocalDateTime.ofInstant(konsultacja.getDataDo().toInstant(),
						ZoneId.systemDefault());

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
				String formattedDataOd = dataOd.format(formatter);
				String formattedDataDo = dataDo.format(formatter);

				model.addAttribute("formattedDataOd", formattedDataOd);
				model.addAttribute("formattedDataDo", formattedDataDo);
				model.addAttribute("konsultacja", konsultacja);

				model.addAttribute("originalDataOd", formattedDataOd);
				model.addAttribute("originalDataDo", formattedDataDo);
				model.addAttribute("originalNrPokoju", konsultacja.getNr_pokoju());
				model.addAttribute("originalCzyOnline", konsultacja.getCzy_online());
				model.addAttribute("originalCzyPubliczne", konsultacja.getCzy_publiczne());

				return "EditKonsultacja";
			}
		}
		return "redirect:/login";
	}

	@PostMapping("/updateKonsultacja")
	public String updateKonsultacja(@ModelAttribute("konsultacja") Konsultacja konsultacja,
			@RequestParam("dataOd") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date dataOd,
			@RequestParam("dataDo") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date dataDo,
			@RequestParam("originalDataOd") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date originalDataOd,
			@RequestParam("originalDataDo") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date originalDataDo,
			@RequestParam("originalNrPokoju") String originalNrPokoju,
			@RequestParam("originalCzyPubliczne") String originalCzyPubliczne,
			@RequestParam("originalCzyOnline") String originalCzyOnline,
			@RequestParam("Nr_realizacji") int selectedRealizacjaId, HttpSession session, Model model) {

		if (checkIfLoggedIn(session, model)) {

			konsultacja.setDataOd(dataOd);
			konsultacja.setDataDo(dataDo);
			Object loggedInUser = session.getAttribute("loggedInUser");
			if (loggedInUser instanceof Prowadzacy) {
				konsultacja.setNr_prowadzacego(((Prowadzacy) loggedInUser).getNr_prowadzacego());
			}

			konsultacja.setNr_realizacji(selectedRealizacjaId);

			daoKonsultacje.update(konsultacja);

			System.out.println(dataOd.toString());

			// Sprawdzanie zmian
			boolean hasChanges = !originalDataOd.equals(dataOd) || !originalDataDo.equals(dataDo)
					|| !originalNrPokoju.equals(String.valueOf(konsultacja.getNr_pokoju()))
					|| !originalCzyPubliczne.equals(konsultacja.getCzy_publiczne())
					|| !originalCzyOnline.equals(konsultacja.getCzy_online());

			// Przy braku zmian, przekierowanie do strony konsultacji
			if (!hasChanges) {
				return "redirect:/konsultacje";
			}

			// W przypadku zmian stworzenie powiadomienia dla Studentów
			StringBuilder notificationMessage = new StringBuilder("Zmodyfikowano konsultacje:\n");
			notificationMessage.append("Poprzednie dane:\n");

			LocalDateTime originalDateTimeOd = LocalDateTime.ofInstant(originalDataOd.toInstant(),
					ZoneId.systemDefault());
			LocalDateTime originalDateTimeDo = LocalDateTime.ofInstant(originalDataDo.toInstant(),
					ZoneId.systemDefault());

			if (!originalDateTimeOd.equals(dataOd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
				String formattedOriginalDataOd = originalDateTimeOd
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				String formattedDataOd = dataOd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				notificationMessage.append("Data od: ").append(formattedOriginalDataOd).append(" (zmienione na: ")
						.append(formattedDataOd).append(")\n");
			}

			if (!originalDateTimeDo.equals(dataDo.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
				String formattedOriginalDataDo = originalDateTimeDo
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				String formattedDataDo = dataDo.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				notificationMessage.append("Data do: ").append(formattedOriginalDataDo).append(" (zmienione na: ")
						.append(formattedDataDo).append(")\n");
			}

			if (!originalNrPokoju.equals(String.valueOf(konsultacja.getNr_pokoju()))) {
				notificationMessage.append("Nr pokoju: ").append(originalNrPokoju).append(" (zmienione na: ")
						.append(konsultacja.getNr_pokoju()).append(")\n");
			}
			if (!originalCzyPubliczne.equals(konsultacja.getFullPubliczne())) {
				notificationMessage.append("Czy publiczne: ").append(originalCzyPubliczne).append(" (zmienione na: ")
						.append(konsultacja.getFullPubliczne()).append(")\n");
			}
			if (!originalCzyOnline.equals(konsultacja.getFullOnline())) {
				notificationMessage.append("Czy online: ").append(originalCzyOnline).append(" (zmienione na: ")
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
			return "redirect:/konsultacje";
		}

		return "redirect:/login";
	}

	@PostMapping("/deleteKonsultacja")
	public String deleteKonsultacja(@RequestParam("nr_konsultacji") int nrKonsultacji, HttpSession session,
			Model model) {
		if (checkIfLoggedIn(session, model)) {
			Konsultacja konsultacja = daoKonsultacje.get(nrKonsultacji);
			if (konsultacja != null) {
				daoKonsultacje.delete(nrKonsultacji);

				// Tworzenie powiadomienia
				StringBuilder notificationMessage = new StringBuilder("Konsultacja zosta³a usuniêta:\n");
				notificationMessage.append("Data od: ").append(konsultacja.getDataOd()).append("\n");
				notificationMessage.append("Data do: ").append(konsultacja.getDataDo()).append("\n");
				notificationMessage.append("Nr pokoju: ").append(konsultacja.getNr_pokoju()).append("\n");
				notificationMessage.append("Czy publiczne: ").append(konsultacja.getFullPubliczne()).append("\n");
				notificationMessage.append("Czy online: ").append(konsultacja.getFullOnline()).append("\n");

				// Zapisywanie powiadomienia
				List<StudentKonsultacja> studentKonsultacjaList = daoStudentKonsultacja
						.getStudentKonsultacjaByNrKonsultacji(konsultacja.getNr_konsultacji());
				if (studentKonsultacjaList != null) {
					for (StudentKonsultacja studentKonsultacja : studentKonsultacjaList) {
						studentKonsultacja.setEdycja(notificationMessage.toString());
						daoStudentKonsultacja.update(studentKonsultacja);
					}
				}
			}
			return "redirect:/konsultacje";
		}
		return "redirect:/login";
	}

	@GetMapping("/SignUpK")
	public String showSingUpKonsultacja(Model model, HttpSession session) {
		if (checkIfLoggedIn(session, model)) {

			List<Konsultacja> listKonsultacja = daoKonsultacje.list();
			model.addAttribute("listKonsultacja", listKonsultacja);

			model.addAttribute("daoPrzedmioty", daoPrzedmioty);

			String errorMessage = (String) session.getAttribute("errorMessage");
			if (errorMessage != null) {
				model.addAttribute("error", errorMessage);
				session.removeAttribute("errorMessage");
			}

			return "SignUpKonsultacja";

		} else {
			return "redirect:/login";
		}
	}

	@PostMapping("/DolaczK")
	public String signInKonsultacja(@RequestParam("nr_konsultacji") int nr_konsultacji, HttpSession session,
			Model model) {
		if (checkIfLoggedIn(session, model)) {

			Object loggedInUser = session.getAttribute("loggedInUser");
			int nrStudenta = ((Student) loggedInUser).getNr_studenta();

			// Sprawdzenie czy student jest ju¿ zapisany na konsultacjê
			if (daoStudentKonsultacja.findByNrStudentaAndNrKonsultacji(nrStudenta, nr_konsultacji) != null) {
				session.setAttribute("errorMessage", "Jesteœ ju¿ na to zapisany!");
				return "redirect:/SignUpK";
			}

			StudentKonsultacja studentKonsultacja = new StudentKonsultacja();
			studentKonsultacja.setNr_studenta(nrStudenta);
			studentKonsultacja.setNr_konsultacji(nr_konsultacji);
			daoStudentKonsultacja.save(studentKonsultacja);

			return "redirect:/SignUpK";

		} else {
			return "redirect:/login";
		}
	}

	@PostMapping("/UsunK")
	public String signOutFromKonsultacja(@RequestParam("nr_konsultacji") int nrKonsultacji, HttpSession session,
			Model model) {
		Object loggedInUser = session.getAttribute("loggedInUser");
		int nrStudenta = ((Student) loggedInUser).getNr_studenta();
		daoStudentKonsultacja.deleteByNrStudentaAndNrKonsultacji(nrStudenta, nrKonsultacji);
		return "redirect:/konsultacje"; // redirect back to the konsultacje page
	}

	/* FORUM */

	@GetMapping("/fora")
	public String getAllFora(HttpSession session, Model model) {
		if (checkIfLoggedIn(session, model)) {

			List<Forum> fora = daoFora.list();
			Collections.reverse(fora);
			model.addAttribute("fora", fora);
			model.addAttribute("przedmiotyDAO", daoPrzedmioty);
			model.addAttribute("foraDAO", daoFora);
			return "fora";

		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/chat/{nrForum1}")
	public String getMessages(@PathVariable("nrForum1") int nrForum1, Model model, HttpSession session) {
		if (checkIfLoggedIn(session, model)) {
			List<Wiadomosc> wiadomosci = WiadomosciDAO.getMessages(nrForum1);
			for (Wiadomosc wiadomosc : wiadomosci) {
				String zdjecieNadawcy = getZdjecieNadawcy(wiadomosc);
				wiadomosc.setZdjecieNadawcy(zdjecieNadawcy);
			}
			model.addAttribute("wiadomosci", wiadomosci);
			model.addAttribute("daoStudenci", daoStudenci);
			model.addAttribute("daoProwadzacy", daoProwadzacy);
			return "forum";
		} else {
			return "redirect:/login";
		}
	}

	@PostMapping("/chat/{nrForum2}")
	public String postMessage(@PathVariable int nrForum2, Model model, @RequestParam(required = false) String tekst,
			HttpSession session) {
		if (checkIfLoggedIn(session, model)) {
			Object loggedInUser = session.getAttribute("loggedInUser");

			Integer nrStudenta = null;
			Integer nrProwadzacego = null;

			if (loggedInUser instanceof Student) {
				nrStudenta = ((Student) loggedInUser).getNr_studenta();
			} else if (loggedInUser instanceof Prowadzacy) {
				nrProwadzacego = ((Prowadzacy) loggedInUser).getNr_prowadzacego();
			}

			String nadawca = (String) session.getAttribute("nadawca");

			Wiadomosc wiadomosc = new Wiadomosc();
			wiadomosc.setNadawca(nadawca);
			wiadomosc.setTekst(tekst);
			wiadomosc.setCzas(new Timestamp(System.currentTimeMillis()));
			wiadomosc.setNr_forum(nrForum2);
			wiadomosc.setNr_studenta(nrStudenta);
			wiadomosc.setNr_prowadzacego(nrProwadzacego);

			String zdjecieNadawcy = getZdjecieNadawcy(wiadomosc);
			wiadomosc.setZdjecieNadawcy(zdjecieNadawcy);

			WiadomosciDAO.addMessage(wiadomosc, nrForum2, nrStudenta, nrProwadzacego);

			return "redirect:/chat/" + nrForum2;
		} else {
			return "redirect:/login";
		}
	}

	public String getZdjecieNadawcy(Wiadomosc wiadomosc) {
		if (wiadomosc.getNr_studenta() != null) {
			Student student = daoStudenci.get(wiadomosc.getNr_studenta());
			if (student != null) {
				Blob zdjecieBlob = student.getZdjecie();
				String zdjecieUrl = convertBlobToBase64(zdjecieBlob);
				return zdjecieUrl;
			}
		} else if (wiadomosc.getNr_prowadzacego() != null) {
			Prowadzacy prowadzacy = daoProwadzacy.get(wiadomosc.getNr_prowadzacego());
			if (prowadzacy != null) {
				Blob zdjecieBlob = prowadzacy.getZdjecie();
				String zdjecieUrl = convertBlobToBase64(zdjecieBlob);
				return zdjecieUrl;
			}
		}
		return null;
	}

}
