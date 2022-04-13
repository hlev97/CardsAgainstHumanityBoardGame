package hu.bme.cah.business_logic;

import java.util.HashMap;
import java.util.ArrayList;

enum State {
    ShowBlackCard,
    ShowWhiteCard,
    Vote
}

public class Game{
    private ArrayList<Pack> cardSet;
    private State state = State.ShowBlackCard;
    private int round;
    private int currentround = 0;
    private ArrayList<User> players;
    private Card blackCard;
    private HashMap<User, Card[]> playerWhiteCards = new HashMap();
    private HashMap<User, Integer> votes = new HashMap();
    private Gatekeeper gatekeeper;

    public Game (ArrayList<User> players, int rounds, ArrayList<Pack> cards) {
        this.players = players;
        round = rounds;
        cardSet = cards;
    }

    private class Gatekeeper { //checks whether a player can do an action in the current context, and how many players finished.
        private HashMap<User, String> done = new HashMap(User, string);
        private numdone = 0;
        private context;

        public Gatekeeper(State contextState){
            context = contextState;
        }

        public boolean checkAction(player){
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
        state = State.ShowWhiteCard
        nextState();
    }

    private void initWhiteCard() {
        playerWhiteCards.clear();
        gatekeeper = new Gatekeeper(state);
    }

    public void ShowWhiteCard(User player, Card[] cards){
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
        gatekeeper = new Gatekeeper(state)
    }

    public void Vote(User player, User target) {
        if(state != gatekeeper.checkAction(player)) //esetleg exception is
            return;
        int number = votes.get(target) == null ? 0 : votes.get(target);
        votes.put(target, number++);
        if(gatekeeper.finished())
            endvote();
    }

    private void endvote(){
        currentround++;
        if(currentround >= round){
            //todo game end logic
        } else {
            state = State.ShowBlackCard
            nextState()
        }
    }
}