import java.util.*;

public class MineSweeper
{

  /*

    // si les cases sont fermé
    -1 = bombs
    0 = pas de autour de la case.
    1 - 8 = entre 1 et 8 cases.

    9 = bombs ouvert
    10 - 18 = case de 0 à 8 ouvert

    18 = case de bombs avec un drapeau
    19 - 26 = case de 0 à 8 avec un drapeau.

  */

  private final int[][] cases;
  private final int x, y;

  /*
    0 = Plus de case et donc on a gagné
    -1 = On est tombé sur une bombe et donc on a perdu.
  */

  private int freeCases;

  private MineSweeper(int x, int y, int bombs)
  {
    this.x = x;
    this.y = y;

    cases = new int[x][y];
    generate(bombs < 10 ? 10 : bombs > 90 ? 90 : bombs, new Random());
  }

  private void generate(int bombs, Random random)
  {
    bombs = x * y * bombs / 100;
    freeCases = x * y - bombs;

    while(bombs != 0)
    {
      int rx = random.nextInt(x);
      int ry = random.nextInt(y);

      if(isBomb(rx, ry)) continue;
      cases[rx][ry] = -1;

      for(int x = -1; x < 2; x++)
      {
        for(int y = -1; y < 2; y++)
        {
          if(isValid(rx+x, ry+y) && !isBomb(rx+x, ry+y))
            cases[rx+x][ry+y]++;
        }
      }
      bombs--;
    }
  }

  private boolean isBomb(int x, int y)
  {
    return isValid(x, y) && (cases[x][y] == -1 || cases[x][y] == 9 || cases[x][y] == 19);
  }

  private boolean isValid(int x, int y)
  {
    return x >= 0 && y >=0 && x < this.x && y < this.y;
  }

  private boolean hasFlag(int x,int y)
  {
    return isValid(x, y) && cases[x][y] > 18;
  }

  public boolean isOpen(int x, int y)
  {
    return isValid(x, y) && cases[x][y] > 8 && cases[x][y] < 19;
  }

  private void print()
  {
    StringBuilder builder = new StringBuilder();

    for(int y = -1; y < this.y; y++)
    {
      builder.append("\t[").append(y == -1 ? " " : y+1).append("]");
      for(int x = 0; x < this.x ; x++)
      {
        builder.append("[").append(y==-1 ? x+1
          : hasFlag(x, y) ? "P"
          : isOpen(x, y) ? isBomb(x, y) ? "+"
            : cases[x][y] == 10 ? " "
            : cases[x][y] - 10
          : "-").append("]");
      }
      builder.append("\n");
    }
    System.out.println(builder.toString());
  }

  private void setFlag(int x, int y)
  {
    if(!isValid(x, y) || isOpen(x, y)) return;
    cases[x][y] += hasFlag(x, y) ? -20 : 20;
  }

  private void open(int x, int y)
  {
    if(!hasFlag(x, y) && isBomb(x, y))
    {
      freeCases = -1;
      for(int xx = 0; xx < this.x; xx++)
      {
        for(int yy = 0; yy < this.y; yy++)
        {
          if(isBomb(xx, yy)) cases[xx][yy] = 9;
        }
      }
    }
    else clear(x, y);
  }

  private void clear(int x, int y)
  {
    if(!isValid(x, y) || isOpen(x, y) || hasFlag(x, y)) return;
    cases[x][y] += 10;
    freeCases--;
    if(cases[x][y] > 10) return;
    clear(x-1, y);
    clear(x+1, y);
    clear(x, y-1);
    clear(x, y+1);
  }

  private void play()
  {
    Scanner scanner = new Scanner(System.in);

    while(freeCases > 0)
    {
      // boucle de jeu
      print();
      System.out.print("Veuillez entrer vos coordonnees x et y");
      String[] line = scanner.nextLine().split(" ");

      try{
        int x = Integer.parseInt(line[0]) - 1;
        int y = Integer.parseInt(line[1]) - 1;

        boolean flag = line.length > 2;
        if(flag) setFlag(x, y);
        else open(x, y);
      }catch(Exception e){
        System.out.println("Valeur non comprise.");
      }
    }

    print();
    System.out.println(freeCases == 0 ? "Felicitation vous avez gagne." : "Vous avez perdu.");
    scanner.close();

    //Fin du progamme.
  }

  public static void main(String... args)
  {
    new MineSweeper(9, 9, 10).play();
  }
}
