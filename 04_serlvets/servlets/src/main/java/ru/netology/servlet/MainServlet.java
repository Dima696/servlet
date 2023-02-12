package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  public  static final String SL = "/";
  public static final String API_PD = "/api/posts/\\d+";
  public  static final String API = "/api/posts";
  public  static final String MGET = "GET";
  public  static final String MPOST = "POST";
  public  static final String MDELETE = "DELETE";
  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {

    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if (method.equals(MGET) && path.equals(API)) {
        controller.all(resp);
        return;
      }
      if (method.equals(MGET) && path.matches(API_PD)) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
        controller.getById(id, resp);
        return;
      }
      if (method.equals(MPOST) && path.equals(API)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(MDELETE) && path.matches(API_PD)) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf(SL)));
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

