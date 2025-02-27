import java.util.StringTokenizer;
//Ángel Gandarias Amadasun 2024
public class HighScore implements Comparable<HighScore> {
    private int points;
    private String user;
    public HighScore() {
    }
    public HighScore(int points, String user) {
        this.points = points;
        this.user = user;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    /**
     * Convierte una linea en un Highscore
     * @param linea String que contiene el highScore
     * @return Highscore
     */
    public static HighScore scoreFromCSV(String linea){
        StringTokenizer st = new StringTokenizer(linea, ";");
        String user = st.nextToken();
        int points = Integer.parseInt(st.nextToken());
        HighScore hs = new HighScore(points, user);
        return hs;
    }
    @Override
    public String toString() {
        return user+":"+points;
    }
   
    @Override
    public int compareTo(HighScore score2) {
        int result = score2.getPoints()-this.getPoints();
        if (result>0){
            result = 1;
        }else if (result<0){
            result = -1;
        }
        return result;
    }
    public String highScoreToCSV(){
        return user+";"+points;
    }
    
    
}
