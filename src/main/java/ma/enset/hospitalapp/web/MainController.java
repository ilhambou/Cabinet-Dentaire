package ma.enset.hospitalapp.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventDateTime;
import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.Act;
import ma.enset.hospitalapp.entities.CalendarObj;
import ma.enset.hospitalapp.entities.Consultation;
import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repository.ActRepository;
import ma.enset.hospitalapp.repository.ConsultationRepository;
import ma.enset.hospitalapp.repository.EventRepository;
import ma.enset.hospitalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.model.Event;
@Controller
public class MainController {

    @Autowired
    private ConsultationRepository consultationRepository;
    @Autowired
    private EventRepository actRepository;
    private PatientRepository patientRepository;
    private ActRepository actRepository2;

    @Autowired
    public MainController(PatientRepository patientRepository, ActRepository actRepository2) {
        this.patientRepository = patientRepository;
        this.actRepository2 = actRepository2;

    }

    private static final String APPLICATION_NAME = "";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar client;

    private static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;
    @Value("${google.client.redirectUri.available.slot}")
    private String redirectURIAvailableSlot;

    private Set<Event> events = new HashSet<>();

    private final int START_HOUR = 8;
    private final int START_MIN = 00;
    private final int END_HOUR = 23;
    private final int END_MIN = 00;

    private static boolean isAuthorised = false;

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    private String authorize(String redirectURL) throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            Details web = new Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR)).build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURL);

        isAuthorised = true;

        return authorizationUrl.build();
    }

    @RequestMapping(value = "/calendar", method = RequestMethod.GET)
    public RedirectView googleConnectionStatus() throws Exception {
        return new RedirectView(authorize(redirectURI));
    }

    @RequestMapping(value = "/calendar", method = RequestMethod.GET, params = "code")
    public String oauth2Callback(@RequestParam(value = "code") String code, Model model) {
        if (isAuthorised) {
            try {

                model.addAttribute("title", "Today's Calendar Events (" + START_HOUR + ":" + START_MIN + " - " + END_HOUR + ":" + END_MIN + ")");
                model.addAttribute("calendarObjs", getTodaysCalendarEventList(code, redirectURI));

            } catch (Exception e) {
                model.addAttribute("calendarObjs", new ArrayList<CalendarObj>());
            }

            return "agenda";
        } else {
            return "/admin/";
        }
    }

    private Calendar getCalendarService(String accessToken) throws IOException {
        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(accessToken);
        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String accessDenied(Model model) {

        model.addAttribute("message", "Not authorised.");
        return "notAuthorized";

    }

    @RequestMapping(value = {"/admin/", "/admin/login", "/admin/logout"}, method = RequestMethod.GET)
    public String login(Model model) {
        isAuthorised = false;

        return "login";
    }


    private List<CalendarObj> getTodaysCalendarEventList(String calenderApiCode, String redirectURL) {
        try {
            com.google.api.services.calendar.model.Events eventList;

            LocalDateTime localDateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
            LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
            LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);

            DateTime date1 = new DateTime(Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()));
            DateTime date2 = new DateTime(Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant()));

            TokenResponse response = flow.newTokenRequest(calenderApiCode).setRedirectUri(redirectURL).execute();
            credential = flow.createAndStoreCredential(response, "userID");
            client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
            Events events = client.events();
            eventList = events.list("primary").setSingleEvents(true).setTimeMin(date1).setTimeMax(date2).setOrderBy("startTime").execute();

            List<Event> items = eventList.getItems();

            CalendarObj calendarObj;
            List<CalendarObj> calendarObjs = new ArrayList<CalendarObj>();

            for (Event event : items) {

                Date startDateTime = new Date(event.getStart().getDateTime().getValue());
                Date endDateTime = new Date(event.getEnd().getDateTime().getValue());


                long diffInMillies = endDateTime.getTime() - startDateTime.getTime();
                int diffmin = (int) (diffInMillies / (60 * 1000));

                calendarObj = new CalendarObj();

                if (event.getSummary() != null && event.getSummary().length() > 0) {
                    calendarObj.setTitle(event.getSummary());
                } else {
                    calendarObj.setTitle("No Title");
                }

                calendarObj.setStartHour(startDateTime.getHours());
                calendarObj.setStartMin(startDateTime.getMinutes());
                calendarObj.setEndHour(endDateTime.getHours());
                calendarObj.setEndMin(endDateTime.getMinutes());
                calendarObj.setDuration(diffmin);

                calendarObj.setStartEnd(sdf.format(startDateTime) + " - " + sdf.format(endDateTime));

                calendarObjs.add(calendarObj);
            }

            return calendarObjs;

        } catch (Exception e) {
            return new ArrayList<CalendarObj>();
        }
    }




    public void addEvent(@RequestParam("startDate") String startTime,
                           @RequestParam("endDateTime") String endTime,
                           @RequestParam("summary") String eventName,
                           Model model) {

        try {


            // Enregistrer l'événement dans la base de données en utilisant le repository
            Calendar calendar = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);

            //
            // Convert the Date objects to DateTime objects using DateTime constructor
            DateTime start = new DateTime(startDate);
            DateTime end = new DateTime(endDate);

         Event   event = new Event()
                    .setSummary(eventName)
                    .setStart(new EventDateTime().setDateTime(start))
                    .setEnd(new EventDateTime().setDateTime(end));

            // Insert the event into the user's calendar
            Event createdEvent = calendar.events().insert("primary", event).execute();
            // Get the ID of the calendar where the event was added
            String calendarId = createdEvent.getICalUID();
            // Get the list of events in the same calendar
            List<CalendarObj> calendarEvents = getTodaysCalendarEventList(calendarId, redirectURI);
            model.addAttribute("message", "Event added successfully!");
            model.addAttribute("eventId", createdEvent.getId());
            model.addAttribute("calendarEvents", calendarEvents);

        } catch (Exception e) {
            // Log the error and return the error page
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while adding the event.");
        }

    }






}