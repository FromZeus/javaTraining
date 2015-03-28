import java.text.ParseException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: osipovku
 * Date: 28.03.15
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class Calc {

    static public void main(String[] args)
    {
        Queue<String> qu = new LinkedList<String>();
        Stack<String> stSymb = new Stack<String>();
        String input;
        Pattern pAll = Pattern.compile("(\\d+|[(\\+\\-\\*\\/)])"),
                pDig = Pattern.compile("\\d+"),
                pFunc = Pattern.compile("[\\+\\-\\*\\/]");
        Matcher m;

        Scanner scanner = new Scanner(System.in);
        try
        {
            input = scanner.nextLine();
            m = pAll.matcher(input);
            while (m.find())
            {
                Matcher mDig = pDig.matcher(m.group()),
                        mFunc = pFunc.matcher(m.group());
                if (mDig.find())
                {
                    qu.add(mDig.group());
                }
                else if (mFunc.find() || m.group() == "(")
                {
                    stSymb.add(m.group());
                }
                else
                {
                    while (stSymb.peek() != "(")
                    {
                        qu.add(stSymb.pop());
                    }
                    stSymb.pop();
                    if (pFunc.matcher(stSymb.peek()).find())
                    {
                        qu.add(stSymb.pop());
                    }
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        while (!qu.isEmpty())
        {
            System.out.println(qu.poll());
        }
    }

}
