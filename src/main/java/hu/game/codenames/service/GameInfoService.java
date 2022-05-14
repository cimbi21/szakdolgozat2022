package hu.game.codenames.service;

import hu.game.codenames.model.GameInfo;
import hu.game.codenames.model.User;
import hu.game.codenames.repository.GameInfoRepository;
import hu.game.codenames.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
    @Slf4j
    public class GameInfoService {
        private GameInfoRepository gameInfoRepository;

        @Autowired
        public void setGameInfoRepository(GameInfoRepository gameInfoRepository) {
            this.gameInfoRepository = gameInfoRepository;
        }

        public GameInfo saveInfo(GameInfo c){
            return gameInfoRepository.save(c);
        }


        public List<GameInfo> getInfos(){
            return gameInfoRepository.findAll();
        }

        public List<GameInfo> myGameInfos(String name){
            List<GameInfo> originalList = gameInfoRepository.findAll();
            List<GameInfo> filteredList = originalList.stream()
                .filter(t -> t.getCreatedBy().equals(name))
                .collect(Collectors.toList());
            return filteredList;
    }


}
