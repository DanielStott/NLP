/*
 * Copyright (c) 2000-2016 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.List;


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
                            	pJSON.process(new JSONObject(
                            			browser.executeJavaScriptAndReturnValue("JSON.stringify($('form').serializeObject());").getStringValue()));
                            }
                        }, false);
                    }
                }
            }
        });
    }
}
