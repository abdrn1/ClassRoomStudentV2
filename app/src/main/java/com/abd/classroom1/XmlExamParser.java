package com.abd.classroom1;

/**
 * Created by Abd on 3/15/2016.
 */

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlExamParser {

    static SAXBuilder saxbuilder;
    static Document examDocument;


    public static List<QuestionItem> examParser(String filePath) throws JDOMException, IOException {

        List<QuestionItem> questions = null;
        saxbuilder = new SAXBuilder();
        File f = new File(filePath);
        examDocument = saxbuilder.build(f);
        Element examTag = examDocument.getRootElement();
        List<Element> questionsList = examTag.getChildren("question");
        questions = new ArrayList<QuestionItem>();


        for (Element e : questionsList) {


            //  start read question
            System.out.println("QUestion Text : " + e.getChildText("q"));
            QuestionItem qitem = new QuestionItem(e.getChildText("q"), e.getAttributeValue("type"), e.getChildText("answer"));

            String ss = e.getAttributeValue("weight");
            //System.out.println("QUestion Weight : " + s);
            if (ss != null) {
                try {
                    qitem.setQuestionWeight(Integer.parseInt(ss));
                } catch (Exception ex) {

                }
            }
            Element choiceslist = e.getChild("choices");

            if (choiceslist != null) {

                List<Element> choiceitems = choiceslist.getChildren("item");
                if (choiceitems != null) {
                    for (Element chitem : choiceitems) {
                        QuestionItem.ChoiceItem qci = new QuestionItem.ChoiceItem();
                        qci.setChoiceText(chitem.getText());
                        System.out.println("Question Choice: " + qci.getChoiceText());
                        String checktxt = chitem.getAttributeValue("stat");
                        if (checktxt.equals("true")) {
                            qci.setChecked(true);
                        }

                        qitem.getChoices().add(qci);

                    }
                }
            }

            questions.add(qitem);


            //System.out.println("Question Text"+qitem.getQuestionText());
            // System.out.println("Question Text"+qitem.getQuestionAnswer());

        }

        return questions;
    }

}
