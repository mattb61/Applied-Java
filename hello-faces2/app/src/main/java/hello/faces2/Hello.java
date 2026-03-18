package hello.faces2;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@RequestScoped
@Named
public class Hello {
    private String name;
    private String greeting;

    public void submit() {
        greeting = "Hello, " + name + "!";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGreeting() {
        return this.greeting;
    }
}
