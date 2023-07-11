package ru.team.backend.archive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArchiveService {

    @Autowired
    private DeletedBookRepository deletedBookRepository;

    @Autowired
    private DeletedUserRepository deletedUserRepository;
    public void addToArchive(DeletedBooks deletedBooks){
        deletedBookRepository.save(deletedBooks);
    }

    public void addToArchive(DeletedUsers deletedUsers){
        deletedUserRepository.save(deletedUsers);
    }
}
