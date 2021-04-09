package com.drift.interview.reporting;

import com.drift.interview.model.Conversation;
import com.drift.interview.model.ConversationResponseMetric;
import com.drift.interview.model.Message;
import java.util.List;

public class ConversationMetricsCalculator {
  public ConversationMetricsCalculator() {}

  /**
   * Returns a ConversationResponseMetric object which can be used to power data visualizations on the front end.
   */
  ConversationResponseMetric calculateAverageResponseTime(Conversation conversation) {
    List<Message> messages = conversation.getMessages();


    double responseTimeSum = 0; // the sum of all responses in conversation
    double responseCount = 0; // the count of all responses in conversation
    double userResponse = 0; // take the time of the user response
    double averageResponse = 0; // the average response time


    boolean isTeamMember = true; // initialize isTeamMember to true because we have to calculate the response time starting from the user


    for (Message message : messages ) {
      /*
      if it's not team member and the last response was from a team member
      record when the user sent the message

       */
      if(!message.isTeamMember()  && isTeamMember ) {
        userResponse = message.getCreatedAt();
        isTeamMember = false;

       }
      /*
      if it's a team member and it's team member turn to response
      increment the counter by 1
      add the current response time to the response time sum


       */

      else if(message.isTeamMember()  && !isTeamMember ){
        responseCount = responseCount + 1;
        responseTimeSum = responseTimeSum + (message.getCreatedAt() - userResponse );
        isTeamMember=true;


      }
    }

    if(responseCount > 0) {
      averageResponse = responseTimeSum / responseCount;
    }




    return ConversationResponseMetric.builder()
        .setConversationId(conversation.getId())
        .setAverageResponseMs(averageResponse)
        .build();
  }
}
