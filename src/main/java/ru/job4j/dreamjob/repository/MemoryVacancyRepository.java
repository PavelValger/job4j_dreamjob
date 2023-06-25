package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {
    private final ConcurrentHashMap<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger();

    public MemoryVacancyRepository() {
        LocalDateTime dateTime = parse(LocalDateTime.now());
        save(new Vacancy(0, "Intern Java Developer", "", dateTime, false, 1));
        save(new Vacancy(0, "Junior Java Developer", "", dateTime, false, 2));
        save(new Vacancy(0, "Junior+ Java Developer", "", dateTime, false, 3));
        save(new Vacancy(0, "Middle Java Developer", "", dateTime, false, 1));
        save(new Vacancy(0, "Middle+ Java Developer", "", dateTime, false, 2));
        save(new Vacancy(0, "Senior Java Developer", "", dateTime, false, 3));
    }

    private LocalDateTime parse(LocalDateTime localDateTime) {
        return LocalDateTime.parse(localDateTime.toString().substring(0, 19));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy)
                -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(),
                vacancy.getCreationDate(), vacancy.getVisible(), vacancy.getCityId())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
