package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;


public class Main {
    private static Faker faker = new Faker();

    private static Person randomPerson() {
        Person person = Person.builder()
                .name(faker.name().fullName())
                .dob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .gender(faker.options().option(Person.Gender.class))
                .address(Address.builder()
                        .country(faker.address().country())
                        .state(faker.address().state())
                        .city(faker.address().city())
                        .streetAddress(faker.address().streetAddress())
                        .zip(faker.address().zipCode())
                        .build())
                .email(faker.internet().emailAddress())
                .profession(faker.company().profession())
                .build();
        return person;
    }
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            for (int i = 0; i < 100; i++) {
                em.persist(randomPerson());
            }
            em.getTransaction().commit();

            em.createQuery("SELECT p FROM Person p", Person.class).getResultList().forEach(System.out::println);
        } finally {
            em.close();
            emf.close();
        }
    }
}
