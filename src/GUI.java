/*
 * Copyright (c) 2000-2016 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMNode;
import com.teamdev.jxbrowser.chromium.dom.events.*;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import org.json.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;


public class GUI 
{
	private Browser browser = new Browser();
    private BrowserView view = new BrowserView(browser);
    private JFrame frame = new JFrame();
    
    public void open()
    {

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        
		
        loadListener(browser);
        createMenuBar();
        
        String workingDir = System.getProperty("user.dir");
        browser.loadURL("file:///" + workingDir + "/GUI.html");
    }
    
    public void close()
    {
    	browser.dispose();
    	frame.dispose();
    }
    
    
    private void loadListener(Browser browser)
    {
        browser.addLoadListener(new LoadAdapter() 
        {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) 
            {
                if (event.isMainFrame())
                {
                    Browser browser = event.getBrowser();
                    DOMDocument document = browser.getDocument();
                    DOMNode analyseButton = document.findElement(By.id("Analyse"));
                    if (analyseButton != null) 
                    {
                        analyseButton.addEventListener(DOMEventType.OnClick, new DOMEventListener() 
                        {
                            public void handleEvent(DOMEvent event) 
                            {
                            	ProcessJSON pJSON = new ProcessJSON();
                            	if(pJSON.process(new JSONObject(
                            			browser.executeJavaScriptAndReturnValue("JSON.stringify($('form').serializeObject());").getStringValue())))
                            	{
                            		browser.executeJavaScript("alert(\"Successfully analyse data.\");");
                            	}
                            	else
                            	{
                            		browser.executeJavaScript("alert(\"Failed to analyse data.\");");
                            	}
                            	
                            }
                        }, false);
                    }
                }
            }
        });
    }
    
    private void createMenuBar() {
    	JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		JMenuBar menubar = new JMenuBar();
		ImageIcon quit = new ImageIcon("exit.png"); // exit picture in the menu
		ImageIcon setting = new ImageIcon("settings.png"); //
		ImageIcon info = new ImageIcon("info.png"); //

		JMenu file = new JMenu("File");


		JMenuItem help = new JMenuItem("Help", info); // help button in the menu
		JMenuItem settings = new JMenuItem("Settings", setting); // setting
																	// button in
																	// the menu
		JMenuItem exit = new JMenuItem("Exit", quit); // exit button in the menu

		exit.setToolTipText("Exit");
		exit.addActionListener((ActionEvent event) -> {

			System.exit(0);
		});

		help.addActionListener((ActionEvent event) -> {

			// add action listener

		});

		file.add(settings);
		file.add(help);
		file.add(exit);

		menubar.add(file);

		frame.setJMenuBar(menubar);
	}
}
