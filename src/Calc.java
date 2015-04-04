import java.text.ParseException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

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
        Stack<String> st = new Stack<String>();
        String input;
        Pattern pAll = Pattern.compile("(\\d+|[(\\+\\-\\*\\/\\^)])"),
                pDig = Pattern.compile("\\d+"),
                pOp = Pattern.compile("[\\+\\-\\*\\/\\^]");
        Matcher m;
        HashMap<String, Integer> priority = new HashMap<String, Integer>();
        priority.put("(", 1); priority.put(")", 1);
        priority.put("+", 2); priority.put("-", 2);
        priority.put("*", 3); priority.put("/", 3);
        priority.put("^", 4);

        Scanner scanner = new Scanner(System.in);
        try
        {
            input = scanner.nextLine();
            m = pAll.matcher(input);
            while (m.find())
            {
                String str = m.group();
                Matcher mDig = pDig.matcher(m.group()),
                        mFunc = pOp.matcher(m.group());
                if (mDig.find())
                {
                    qu.add(mDig.group());
                }

                if (mFunc.find())
                {
                    while (!st.empty() &&
                            priority.get(mFunc.group()) <= priority.get(st.peek()))
                    {
                        qu.add(st.pop());
                    }
                    st.push(mFunc.group());
                }

                if (m.group().equals("("))
                {
                    st.push(m.group());
                }

                if (m.group().equals(")"))
                {
                    while (!st.peek().equals("("))
                    {
                        qu.add(st.pop());
                    }
                    st.pop();
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        while (!st.empty())
        {
            qu.add(st.pop());
        }

        /*while (!qu.isEmpty())
        {
            System.out.println(qu.poll());
        }*/
        Float res;
        while (!qu.isEmpty())
        {
            st.push(qu.poll());
            if (pOp.matcher(st.peek()).find())
            {
                res = ;
            }
        }
    }

}
