package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Set;
import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.White;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game implements GameUserInterface{
    private List<Set> cards;
    private State state = State.ShowBlackCard;
    private int round;
    private int currentround = 1;
    private List<User> players;
    private Black blackCard;
    private HashMap<User, White[]> playerWhiteCards = new HashMap();
    private HashMap<User, Integer> votes = new HashMap();
    private Gatekeeper gatekeeper = new Gatekeeper(State.Vote);
    private Room room;
    private Random rand = new Random();

    //todo amount of white cards given to a black card not checked. users not notified of cards dealt - this might not be necessary

    public Game (List<User> players, int rounds, List<Set> cards, Room room) {
        this.players = players;
        this.cards = cards;
        this.room = room;
        round = rounds;

        for(User player : players){
            player.NotifyGameBegin(players, rounds);
        }
        nextState();
    }

    private class Gatekeeper { //checks whether a player can do an action in the current context, and how many players finished.
        private HashMap<User, String> done = new HashMap();
        private int numdone = 0;
        private State context;

        public Gatekeeper(State contextState){
            context = contextState;
        }

        public boolean checkAction(User player){
            if(!players.contains(player))
                return false;
            if(state == context && done.get(player) == null){
                done.put(player, "");
                numdone++;
                return true;
            }
            else
                return false;
        }

        public boolean finished(){
            return numdone == players.size();
        }
    }

    private void nextState(){
        switch (state){
            case ShowBlackCard:
                showBlackCard();
                break;
            case ShowWhiteCard:
                initWhiteCard();
                break;
            case Vote:
                initVote();
                break;
            default:
                break;
        }
    }

    private void showBlackCard(){
        //todo selection logic
        List<Black> blackcards = null;

        blackCard = blackcards.get(rand.nextInt(blackcards.size()));
        //todo itt removeoljuk?

        state = State.ShowWhiteCard;
        nextState();
    }

    private void initWhiteCard() {
        playerWhiteCards.clear();
        gatekeeper = new Gatekeeper(state);

        for(User player : players){
            player.NotifyRoundStart(currentround, blackCard);
        }
    }

    public synchronized void ShowWhiteCard(User player, White[] cards){
        if(!gatekeeper.checkAction(player)) //esetleg dobhat vmi hibat is.
            return;
        for(White card : cards){
            if(!player.WhiteCards.contains(card))
                return;
        }
        for(White card : cards){
            player.WhiteCards.remove(card);
        }
        playerWhiteCards.put(player, cards);
        if(gatekeeper.finished()) {
            state = State.Vote;
            nextState();
        }
    }

    private void initVote(){
        votes.clear();
        gatekeeper = new Gatekeeper(state);

        for(User player : players){
            player.NotifyVote();
        }
    }

    public synchronized void Vote(User player, User target) {
        if(!gatekeeper.checkAction(player)) //esetleg exception is
            return;
        int number = votes.get(target) == null ? 0 : votes.get(target);
        votes.put(target, ++number);
        if(gatekeeper.finished())
            endvote();
    }

    private void endvote(){
        currentround++;
        if(currentround > round){
            //todo game end logic
            List<User> scoreboard = null;

            for(User player : players){
                player.NotifyGameEnd(scoreboard);
            }
            room.GameEnded();
        } else {
            state = State.ShowBlackCard;
            nextState();
        }
    }

    private void DealCards(User player, List<White> cards, int number){

    }
}