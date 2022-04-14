package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Set;
import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.White;

import java.util.HashMap;
import java.util.ArrayList;

public class Game implements GameUserInterface{
    private ArrayList<Set> cards;
    private State state = State.ShowBlackCard;
    private int round;
    private int currentround = 1;
    private ArrayList<User> players;
    private Black blackCard;
    private HashMap<User, White[]> playerWhiteCards = new HashMap();
    private HashMap<User, Integer> votes = new HashMap();
    private Gatekeeper gatekeeper = new Gatekeeper(State.Vote);

    public Game (ArrayList<User> players, int rounds, ArrayList<Set> cards) {
        this.players = players;
        this.cards = cards;
        round = rounds;

        for(User player : players){
            player.NotifyGameBegin(players, rounds);
        }
    }

    private class Gatekeeper { //checks whether a player can do an action in the current context, and how many players finished.
        private HashMap<User, String> done = new HashMap();
        private int numdone = 0;
        private State context;

        public Gatekeeper(State contextState){
            context = contextState;
        }

        public boolean checkAction(User player){
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
        blackCard = null;

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
        votes.put(target, number++);
        if(gatekeeper.finished())
            endvote();
    }

    private void endvote(){
        currentround++;
        if(currentround > round){
            //todo game end logic
            User winner = null;

            for(User player : players){
                player.NotifyGameEnd(winner);
            }
        } else {
            state = State.ShowBlackCard;
            nextState();
        }
    }
}