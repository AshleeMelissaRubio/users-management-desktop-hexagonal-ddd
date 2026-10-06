package com.jcaa.usersmanagement;

import com.jcaa.usersmanagement.infrastructure.config.DependencyContainer;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.TeacherManagementCli;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.UserManagementCli;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public final class Main {

  public static void main(final String[] args) {
    log.info("Starting System...");
    final DependencyContainer container = new DependencyContainer();

    try (final Scanner scanner = new Scanner(System.in)) {
      final ConsoleIO console = new ConsoleIO(scanner, System.out);
      boolean running = true;

      while (running) {
        console.println("\n==================================");
        console.println("       SYSTEM MAIN MENU           ");
        console.println("==================================");
        console.println("  [1] Users Management");
        console.println("  [2] Teachers Management");
        // console.println("  [3] TFC Management");
        console.println("  [0] Exit");
        console.println("==================================");

        final String option = console.readRequired("Option: ");

        switch (option) {
          case "1" -> new UserManagementCli(container.userController(), console).start();
          case "2" -> new TeacherManagementCli(container.teacherController(), console).start();
          case "0" -> {
            running = false;
            console.println("Goodbye!");
          }
          default -> console.println("Invalid option.");
        }
      }
    }
  }
}