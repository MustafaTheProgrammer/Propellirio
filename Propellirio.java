import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.util.Random;

class Propellirio extends JFrame implements KeyListener{
    boolean leftKey;
    boolean upKey;
    boolean rightKey;
    boolean downKey;
    boolean spaceKey;
    int spaceTimer = 0;
    public static int width = 35;
    public static int height = 25;
    public static char[][] pixels = new char[height][width];
    public static int[] pos = {0, 0};
    public static int[] vel = {0, 0};
    public static int[] acc = {1, -1};
    public static boolean dead = false;
    public static int animationTimer = 0;
    public static Random random1 = new Random();
    public static Random random2 = new Random();
    public static int[] rocket1Pos = {-5, -19};
    public static int rocket1Vel;
    public static int[] rocket2Pos = {40, -9};
    public static int rocket2Vel;
    public static int score = 0;
    public static int highScore = 0;
    String gameState = "title screen";

    //images
    char[][] titleText = {{'P', 'r', 'o', 'p', 'e', 'l', 'l', 'i', 'r', 'i', 'o'},
                          {'S', 'p', 'a', 'c', 'e', ' ', 't', 'o', ' ', ' ', ' '},
                          {'s', 't', 'a', 'r', 't', ' ', ' ', ' ', ' ', ' ', ' '}};

    char[][] gameOverText = {{'G', 'a', 'm', 'e', ' ', 'O', 'v', 'e', 'r'},
                             {'S', 'p', 'a', 'c', 'e', ' ', 't', 'o', ' '},
                             {'r', 'e', 's', 't', 'a', 'r', 't', ' ', ' '}};
    
    char[][] player1 = {{'x', '-', 'x', '-', 'x'},
                        {'x', 'x', '|', 'x', 'x'},
                        {'x', '-', '-', '-', 'x'},
                        {'|', 'O', 'U', 'O', '|'},
                        {'x', '_', '_', '_', 'x'}};
    
    char[][] player2 = {{'x', 'x', '-', 'x', 'x'},
                        {'x', 'x', '|', 'x', 'x'},
                        {'x', '-', '-', '-', 'x'},
                        {'|', 'O', 'U', 'O', '|'},
                        {'x', '_', '_', '_', 'x'}};

    char[][] playerDead = {{'x', '-', 'x', '-', 'x'},
                           {'x', 'x', '|', 'x', 'x'},
                           {'x', '-', '-', '-', 'x'},
                           {'|', 'X', 'o', 'X', '|'},
                           {'x', '_', '_', '_', 'x'}};

    char[][] rocket = {{'^', 'x', 'x', 'x', 'x'},
                       {'#', '#', '#', 'x', 'x'},
                       {'#', '#', '#', '#', '#'},
                       {'#', '#', '#', 'x', 'x'},
                       {'v', 'x', 'x', 'x', 'x'}};

    public Propellirio(){
        this.setTitle("Propellirio");
        this.setSize(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setVisible(true);
        gameLoop();
    }

    private void gameLoop(){
        while (1==1){
            System.out.print("\033[H\033[2J");
            System.out.flush();

            for (int i = 0; i < height; i++){
                for (int j = 0; j < width; j++){
                    pixels[i][j] = '.';
                }
            }
            
            if (gameState == "title screen"){
                drawSprite(titleText, 9, -7, 11, 3, ' ', false, true);
                if (spaceTimer > 0){
                    spaceTimer--;
                }
                if (spaceKey && spaceTimer == 0){
                    gameState = "game";
                    pos[0] = 14;
                    pos[1] = -10;
                    vel[0] = 0;
                    vel[1] = 0;
                    rocket1Pos[0] = -5;
                    rocket1Pos[1] = -19;
                    rocket1Vel = random1.nextInt(3)-3;
                    rocket2Pos[0] = 40;
                    rocket2Pos[1] = -9;
                    rocket2Vel = random2.nextInt(3)+1;
                    dead = false;
                    score = 0;
                }
            }
            else if (gameState == "game"){
                if (!dead){
                    if (upKey){
                    vel[1] = 3;
                    }
                
                    vel[1] += acc[1];
                    pos[1] += vel[1];

                
                    if (rightKey){
                        vel[0] += acc[0];
                        if (vel[0] > 4){
                            vel[0] = 4;
                        }
                    }
                    if (!rightKey && vel[0] > 0){
                        vel[0] -= acc[0];
                    }
                
                    if (leftKey){
                        vel[0] -= acc[0];
                        if (vel[0] < -4){
                            vel[0] = -4;
                        }
                    }
                    if (!leftKey && vel[0] < 0){
                        vel[0] -= acc[0];
                    }
                    pos[0] += vel[0];

                    //boundaries + death!
                    if (pos[0] < 0){
                        pos[0] = 0;
                        vel[0] = 0;
                    }
                    if (pos[0] + 4 > width - 1){
                        pos[0] = width - 5;
                        vel[0] = 0;
                    }
                    if (pos[1] + 4 > 0){
                        pos[1] = -4;
                        vel[1] = 0;
                    }
                    if (pos[1] < -1 * (height - 1)){
                        pos[1] = -1 * (height - 1);
                        dead = true;
                        vel[1] = 4;
                        spaceTimer = 10;
                    }
                    animationTimer++;
                    if (animationTimer > 10){
                        animationTimer = 1;
                    }
                    if (animationTimer <= 5){
                        drawSprite(player1, pos[0], pos[1], 5, 5, 'x', false, true);
                    }
                    else if (animationTimer > 5){
                        drawSprite(player2, pos[0], pos[1], 5, 5, 'x', false, true);
                    }
                    score++;
                }
                else{
                    vel[1]--;
                    pos[1] += vel[1];
                    if (pos[1] < -39){
                        gameState = "game over";
                    }
                    drawSprite(playerDead, pos[0], pos[1], 5, 5, 'x', false, true);
                }

                //rockets
                rocket1Pos[0] += rocket1Vel;
                if (rocket1Pos[0] > 40){
                    rocket1Pos[0] = 40;
                    rocket1Vel = random1.nextInt(3)-3;
                    rocket1Pos[1] = random1.nextInt(25)-29;
                }
                else if (rocket1Pos[0] < -5){
                    rocket1Pos[0] = -5;
                    rocket1Vel = random1.nextInt(3)+1;
                    rocket1Pos[1] = random1.nextInt(25)-29;
                }
                if (pos[0] < rocket1Pos[0] + 4 && pos[0] + 4 > rocket1Pos[0] && pos[1] < rocket1Pos[1] + 4 && pos[1] + 4 > rocket1Pos[1] && !dead){
                    dead = true;
                    vel[1] = 4;
                }
                drawSprite(rocket, rocket1Pos[0], rocket1Pos[1], 5, 5, 'x', rocket1Vel < 0, true);

                rocket2Pos[0] += rocket2Vel;
                if (rocket2Pos[0] > 40){
                    rocket2Pos[0] = 40;
                    rocket2Vel = random1.nextInt(3)-3;
                    rocket2Pos[1] = random1.nextInt(25)-29;
                }
                else if (rocket2Pos[0] < -5){
                    rocket2Pos[0] = -5;
                    rocket2Vel = random1.nextInt(3)+1;
                    rocket2Pos[1] = random1.nextInt(25)-29;
                }
                if (pos[0] < rocket2Pos[0] + 4 && pos[0] + 4 > rocket2Pos[0] && pos[1] < rocket2Pos[1] + 4 && pos[1] + 4 > rocket2Pos[1] && !dead){
                    dead = true;
                    vel[1] = 4;
                }
                drawSprite(rocket, rocket2Pos[0], rocket2Pos[1], 5, 5, 'x', rocket2Vel < 0, true);
            }
            else if (gameState == "game over"){
                drawSprite(gameOverText, 9, -7, 9, 3, 'x', false, true);
                if (spaceTimer > 0){
                    spaceTimer--;
                }
                if (spaceKey && spaceTimer == 0){
                    gameState = "title screen";
                    if (score > highScore){
                        highScore = score;
                    }
                    spaceTimer = 10;
                }
            }
            if (gameState == "title screen"){
                System.out.println("Score: " + score + " highScore: " + highScore);
            }
            else if (gameState == "game"){
                System.out.println("Score: " + score);
            }
            for (int i = 0; i < height; i++){
                for (int j = 0; j < width; j++){
                    System.out.print(pixels[i][j]);
                }
                System.out.println();
            }
            
            try{
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        new Propellirio();
    }
    
    static void drawSprite(char[][] sprite, int x, int y, int width, int height, char transperant, boolean hFlip, boolean vFlip){
        for (int i = y; i < y + height; i++){
            for (int j = x; j < x + width; j++){
                if (hFlip == false && vFlip == false){
                    if (sprite[i-y][j-x] != transperant){
                        setPixel(j, i, sprite[i-y][j-x]);
                    }
                }
                else if (hFlip == true && vFlip == false){
                    if (sprite[i-y][Math.abs(j-(x+(width-1)))] != transperant){
                        setPixel(j, i, sprite[i-y][Math.abs(j-(x+(width-1)))]);
                    }
                }
                else if (hFlip == false && vFlip == true){
                    if (sprite[Math.abs(i-(y+(height-1)))][j-x] != transperant){
                        setPixel(j, i, sprite[Math.abs(i-(y+(height-1)))][j-x]);
                    }
                }
                else if (hFlip == true && vFlip == true){
                    if (sprite[Math.abs(i-(y+(height-1)))][Math.abs(j-(x+(width-1)))] != transperant){
                        setPixel(j, i, sprite[Math.abs(i-(y+(height-1)))][Math.abs(j-(x+(width-1)))]);
                    }
                }
            }
        }
    }
    
    static void setPixel(int x, int y, char sym){
        if (x >= 0 && x <= width - 1 && y <= 0 && y >= -1 * (height - 1)){
            pixels[Math.abs(y)][x] = sym;
        }
    }
    
    public void keyTyped(KeyEvent e){

    }
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){
            case 37: leftKey = true;
            break;
            case 38: upKey = true;
            break;
            case 39: rightKey = true;
            break;
            case 32: spaceKey = true;
            break;
        }
    }
    public void keyReleased(KeyEvent e){
        switch (e.getKeyCode()){
            case 37: leftKey = false;
            break;
            case 38: upKey = false;
            break;
            case 39: rightKey = false;
            break;
            case 32: spaceKey = false;
            break;
        }
    }
}