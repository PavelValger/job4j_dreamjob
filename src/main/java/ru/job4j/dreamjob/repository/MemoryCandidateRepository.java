package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {
    private int nextId = 1;
    private final Map<Integer, Candidate> candidates = new HashMap<>();

    public MemoryCandidateRepository() {
        LocalDateTime dateTime = parse(LocalDateTime.now());
        save(new Candidate(0, "Pavel", "", dateTime));
        save(new Candidate(0, "Ivan", "", dateTime));
        save(new Candidate(0, "Anna", "", dateTime));
        save(new Candidate(0, "Stanislav", "", dateTime));
        save(new Candidate(0, "Kristina", "", dateTime));
        save(new Candidate(0, "Petr", "", dateTime));
    }

    private LocalDateTime parse(LocalDateTime localDateTime) {
        return LocalDateTime.parse(localDateTime.toString().substring(0, 19));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldCandidate)
                -> new Candidate(oldCandidate.getId(), candidate.getName(),
                candidate.getDescription(), candidate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
