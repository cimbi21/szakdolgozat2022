package hu.game.codenames.service;

import hu.game.codenames.model.GameInfo;
import hu.game.codenames.model.Guess;
import hu.game.codenames.repository.GameInfoRepository;
import hu.game.codenames.repository.GuessRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GuessService {
    private GuessRepository guessRepository;

    @Autowired
    public void setguessRepository(GuessRepository guessRepository) {
        this.guessRepository = guessRepository;
    }

    public Guess save(Guess c){
        return guessRepository.save(c);
    }


    public List<Guess> get(){
        return guessRepository.findAll();
    }
}
