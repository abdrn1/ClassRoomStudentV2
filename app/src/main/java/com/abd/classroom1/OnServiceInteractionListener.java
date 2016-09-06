package com.abd.classroom1;

import java.util.List;

/**
 * Created by abdrn on 9/4/2016.
 */
public interface OnServiceInteractionListener {
    public void updateMessageViewer(String uID);
    public  void showExamViewer(FileChunkMessageV2 fcmv2, List<QuestionItem> tQuestionsList);
    public void updateClientsList();
   // public void updateExamViewer();
   // public void updateCientsList();
}
