import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Word implements Serializable{
    Word(String value) {
        //TODO Auto-generated constructor stub
        this.value = value;
    }


    public String value;    

    public boolean isGuessed(char letter){
        //check if letter is in word
        //if it is, add it to guessedLetters
        //if it isn't, decrement remainingGuesses

        for(int i = 0; i < value.length(); i++){
            if(value.charAt(i) == letter){
                return true;
            }
        }

        return false;
    }

    public List<Integer> revealLetter(char letter){
        //reveals all instances of letter in word
        //returns list of indices of revealed letters
        //if letter is not in word, return empty list
        //initialize empty list of integers
        //iterate through word
        //if letter is in word, add index to list
        //return list

        List<Integer> indices = new ArrayList<Integer>();
        for(int i = 0; i < value.length(); i++){
            if(value.charAt(i) == letter){
                indices.add(i);
            }
        }

        return indices;
    }

    public boolean isComplete(){
        //check if all letters in word have been guessed
        //if they have, return true
        //if not, return false

        for(int i = 0; i < value.length(); i++){
            if(!isGuessed(value.charAt(i))){
                return false;
            }
        }

        return true;

    }

    public void resetWord(){
        //reset word to initial state, making sure that there is no intersection with the wordguesser class before merging
        //reset guessedLetters
        //reset remainingGuesses
        //reset correctGuesses
        //reset categoryAttempts
        
    }
}

