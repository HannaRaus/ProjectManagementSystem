package ua.goit.jdbc.command;

import ua.goit.jdbc.config.DatabaseConnectionManager;
import ua.goit.jdbc.dao.*;
import ua.goit.jdbc.dto.*;
import ua.goit.jdbc.exceptions.DAOException;
import ua.goit.jdbc.service.Service;
import ua.goit.jdbc.view.View;

import java.util.Arrays;


public abstract class Commands {
    private final View view;
    private final Service<Customer> customerService;
    private final Service<Company> companyService;
    private final Service<Project> projectService;
    private final Service<Developer> developerService;
    private final Service<Skill> skillService;

    public Commands(View view, DatabaseConnectionManager connectionManager) {
        this.view = view;
        this.customerService = new Service<>(new CustomerDAO(connectionManager));
        this.companyService = new Service<>(new CompanyDAO(connectionManager));
        this.projectService = new Service<>(new ProjectDAO(connectionManager));
        this.developerService = new Service<>(new DeveloperDAO(connectionManager));
        this.skillService = new Service<>(new SkillDAO(connectionManager));
    }

    protected Service<Customer> getCustomerService() {
        return customerService;
    }

    protected Service<Company> getCompanyService() {
        return companyService;
    }

    protected Service<Project> getProjectService() {
        return projectService;
    }

    protected Service<Developer> getDeveloperService() {
        return developerService;
    }

    protected Service<Skill> getSkillService() {
        return skillService;
    }

    protected long getLongFromConsole(String message) {
        long number = 0;
        boolean isFieldBlank = true;
        while (isFieldBlank) {
            try {
                view.write(message);
                number = Long.parseLong(view.read());
                if (number <= 0) {
                    view.write("Number is less, than zero, please, enter the correct one");
                } else {
                    isFieldBlank = false;
                }
            } catch (Exception e) {
                view.write("Wrong format, please, enter integer.");
            }
        }
        return number;
    }

    protected double getDoubleFromConsole(String message) {
        double number = 0;
        boolean isFieldBlank = true;
        while (isFieldBlank) {
            try {
                view.write(message);
                number = Double.parseDouble(view.read());
                if (number <= 0) {
                    view.write("Number is less, than zero, please, enter the correct one");
                } else {
                    isFieldBlank = false;
                }
            } catch (Exception e) {
                view.write("Wrong format, please, enter integer.");
            }
        }
        return number;
    }

    protected Branch getBranchFromConsole() {
        Branch branch = null;
        boolean isFieldBlank = true;
        while (isFieldBlank) {
            try {
                view.write("Enter the programing language");
                branch = Branch.findByName(view.read());
                isFieldBlank = false;
            } catch (Exception e) {
                view.write("Wrong branch, choose from list below");
                Arrays.stream(Branch.values()).map(Branch::getName).forEach(System.out::println);
            }
        }
        return branch;
    }

    protected SkillLevel getLevelFromConsole() {
        SkillLevel level = null;
        boolean isFieldBlank = true;
        while (isFieldBlank) {
            try {
                view.write("Enter the level of knowledge");
                level = SkillLevel.findByName(view.read().toLowerCase());
                isFieldBlank = false;
            } catch (Exception e) {
                view.write("Wrong level, choose from list below");
                Arrays.stream(SkillLevel.values()).map(SkillLevel::getName).forEach(System.out::println);
            }
        }
        return level;
    }

    protected Sex getGenderFromConsole() {
        Sex level = null;
        boolean isFieldBlank = true;
        while (isFieldBlank) {
            try {
                view.write("Enter the developer gender");
                level = Sex.findByName(view.read().toLowerCase());
                isFieldBlank = false;
            } catch (Exception e) {
                view.write("Wrong data, choose from list below");
                Arrays.stream(Sex.values()).map(Sex::getName).forEach(System.out::println);
            }
        }
        return level;
    }

    protected  <T> T getByID(Service<T> service, String entityName) {
        long id = getLongFromConsole(String.format("Enter %s id", entityName));
        T entity = null;
        try {
            entity = service.findById(id);
            view.write("The " + entity.getClass().getSimpleName() + " with id [" + id + "] is \n" + entity + "\n");
        } catch (DAOException ex) {
            view.write(ex.getMessage());
        }
        return entity;
    }
}
