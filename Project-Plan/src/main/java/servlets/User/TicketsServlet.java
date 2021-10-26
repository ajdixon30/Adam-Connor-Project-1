package servlets.User;

import Models.Ticket;
import Models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import services.TicketService;
import services.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TicketsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Ticket> tickets = TicketService.getAllTickets();
        ObjectMapper mapper = new ObjectMapper();
        resp.getWriter().write(mapper.writeValueAsString(tickets));
        resp.setContentType("application/json");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //int user_id = Integer.parseInt(req.getParameter("user_id"));
        InputStream requestBody = req.getInputStream();
        Scanner sc = new Scanner(requestBody, StandardCharsets.UTF_8.name());
        String jsonText = sc.useDelimiter("\\A").next();
        ObjectMapper mapper =  new ObjectMapper();
        User user = mapper.readValue(jsonText, User.class);

        //User user = new User();
        //user.setUser_id(user_id);
        System.out.println("DEBUG - user_id: " + user.getUser_id());
        user = UserService.getUserByID(user);
        System.out.println("DEBUG - user_id: " + user.getUser_id() + ", " + user.getFirst_name());


        List<Ticket> tickets;
        tickets = TicketService.getTicketsByID(user);

        if (tickets.size() == 0){
            PrintWriter out = resp.getWriter();
            out.println("You have not purchased any tickets");
        } else {
            //ObjectMapper mapper = new ObjectMapper();
            resp.getWriter().write(mapper.writeValueAsString(tickets));
            resp.setContentType("application/json");
        }

    }
}
