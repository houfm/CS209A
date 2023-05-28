package cse.java2.project.service;

import cse.java2.project.model.Answer;
import cse.java2.project.model.Question;
import cse.java2.project.repository.AnswerRepository;
import cse.java2.project.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(AnswerRepository answerRepository,
                         QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public Optional<Answer> getAnswer(Long answerId) {
        return answerRepository.findById(answerId);
    }

    public double getQuestionGetAcceptedAnswerPercentage() {
        long acceptedNum =  answerRepository.countByAccepted(true);
        long totalQuestionNum = answerRepository.count();
        double percentage = (double) acceptedNum / totalQuestionNum;
        return percentage;
    }

    public int[] getTimeGapDistribution() {
        List<Answer> answers = answerRepository.findAnswerByAccepted(true);
        int cnt = answers.size();
        int[] timeGap = new int[8];
        // 0 0, 0.25h
        // 1 0.25 0.5h
        // 2 0.5 0.75h
        // 3 0.75 1
        // 4 1h 6h
        // 5 6, 12h
        // 6 12, 24h
        // 7 24+
        for (int i = 0; i < cnt; i++) {
            Answer answer = answers.get(i);
            long questionId = answer.getQuestionId();
            Question question = questionRepository.findQuestionByQuestionId(questionId);
            Timestamp questionTime = question.getCreationDate();
            Timestamp answerTime = answer.getCreationDate();
            // 1 week = 604800000 ms
            // 1 min = 60000 ms
            // 15 min = 900000 ms
            // 30 min = 1800000 ms
            // 1 h = 3600000 ms
            long gap = (answerTime.getTime() - questionTime.getTime()) / 900000;
            if (gap ==0) {
                timeGap[0]++;
            } else if (gap == 1) {
                timeGap[1]++;
            } else if (gap == 2) {
                timeGap[2]++;
            } else if (gap == 3) {
                timeGap[3]++;
            } else if (gap <= 24) {
                timeGap[4]++;
            } else if (gap <= 48) {
                timeGap[5]++;
            } else if (gap <= 96) {
                timeGap[6]++;
            } else {
                timeGap[7]++;
            }
        }
        return timeGap;
    }

    public double getPercentageOfQuestionWithMoreVoteOnNonAcceptedAnswer() {
        long totalQuestionNum = questionRepository.count();
        long moreVoteNum = 0;
        List<Answer> acceptedAnswers = answerRepository.findAnswerByAccepted(true);
        for (Answer answer : acceptedAnswers) {
            long questionId = answer.getQuestionId();
            int acceptedAnswerVote = answer.getScore();
            int maxVote = acceptedAnswerVote;
            List<Answer> answers = answerRepository.findAnswersByQuestionId(questionId);
            for (Answer a : answers) {
                if (a.getScore() > maxVote) {
                    maxVote = a.getScore();
                }
            }
            if (maxVote > acceptedAnswerVote) {
                moreVoteNum++;
            }
        }
        return (double) moreVoteNum / totalQuestionNum;
    }
}
