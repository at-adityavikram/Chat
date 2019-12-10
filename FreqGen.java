package freqgen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class FreqGen 
{
    static int n = 0;
    static int ctr = 0;
    static int yu = 0;
    static int cur = 0;
    static int prev = 0;
    
    public static void main(String[] args) 
    {
        JFrame f = new JFrame("Frequency Generator");
        f.setSize(500, 500);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel l = new JLabel();
        l.setBounds(290, 100, 50, 50);
        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                if (prev != n)
                {
                    prev = n;
                    ctr = 0;
                    yu = 0;
                    cur = 0;
                }
                l.setText("██");
                if (ctr == 0)
                {
                    if (yu != n)
                    {
                        l.setBounds(290 - yu, 100, 50, 50);
                        yu += 1;
                    }
                    else
                    {
                        ctr = 1;
                        cur = 290 - yu;
                        yu = 0;
                    }
                }
                else if (ctr == 1)
                {
                    if (yu != n)
                    {
                        l.setBounds(cur + yu, 100, 50, 50);
                        yu += 1;
                    }
                    else
                    {
                        ctr = 2;
                        yu = 0;
                        cur = 0;
                    }
                }
                else if (ctr == 2)
                {
                    if (yu != n)
                    {
                        l.setBounds(290 + yu, 100, 50, 50);
                        yu += 1;
                    }
                    else
                    {
                        ctr = 3;
                        cur = 290 + yu;
                        yu = 0;
                    }
                }
                else if (ctr == 3)
                {
                    if (yu != n)
                    {
                        l.setBounds(cur - yu, 100, 50, 50);
                        yu += 1;
                    }
                    else
                    {
                        ctr = 0;
                        yu = 0;
                        cur = 0;
                    }
                }
            }
        };
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(r, 0, 1, TimeUnit.MILLISECONDS);
        JTextField t = new JTextField();
        t.setBounds(100, 300, 100, 30);
        JButton b = new JButton();
        b.setBounds(100, 400, 50, 50);
        b.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String h = t.getText().toString();
                n = Integer.parseInt(h);
            }
        });
        f.add(t);
        f.add(l);
        f.add(b);
        f.show();
    }
    
}
