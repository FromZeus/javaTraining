import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: osipovku
 * Date: 04.04.15
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class MyCalc <T extends Number>{
    Pattern pAll, pDig, pOp;
    Queue<String> qu;
    HashMap<String, Integer> priority;

    MyCalc()
    {
        qu = new LinkedList<String>();
        priority = new HashMap<String, Integer>();
        priority.put("(", 1); priority.put(")", 1);
        priority.put("+", 2); priority.put("-", 2);
        priority.put("*", 3); priority.put("/", 3);
        priority.put("^", 4);
        pAll = Pattern.compile("(\\d+|[(\\+\\-\\*\\/\\^)])");
        pDig = Pattern.compile("\\d+");
        pOp = Pattern.compile("[\\+\\-\\*\\/\\^]");
    }

    public Double add(T a, T b)
    {
        //if (a.getClass() == Double.class)
        return a.doubleValue() + b.doubleValue();
    }

    public Double mult(T a, T b)
    {
        return a.doubleValue() * b.doubleValue();
    }

    public Double div(T a, T b)
    {
        return a.doubleValue() / b.doubleValue();
    }

    public Double mod(T a, T b)
    {
        return a.doubleValue() % b.doubleValue();
    }

    void calculate()
    {
        Double res = Double.valueOf(0);
        Stack<T> st = new Stack<T>();
        while (!qu.isEmpty())
        {
            if (pOp.matcher(qu.peek()).find())
            {
                if (qu.peek() == "+")
                    res += add(st.pop(), st.pop());

                qu.poll();
            }

            if (pDig.matcher(qu.peek()).find())
            {
                //st.push(qu.peek());
            }
        }
    }

    public Queue<String> Shunting_yard(String input)
    {
        Stack<String> st = new Stack<String>();
        Matcher m;
        try
        {
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

        return qu;
    }
}
