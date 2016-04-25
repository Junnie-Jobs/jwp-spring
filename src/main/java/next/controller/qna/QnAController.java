package next.controller.qna;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.Result;
import next.model.User;
import next.service.QnaService;

@Controller
public class QnAController {
	@Resource
	private QuestionDao questionDao;
	@Resource
	private AnswerDao answerDao;
	@Resource
	private QnaService qnaService;
	private static final Logger log = LoggerFactory.getLogger(QnAController.class);

	// 1.ShowQuestionController
	@RequestMapping(value = "/qna/show", method = RequestMethod.GET)
	public ModelAndView qnaShow(@RequestParam long questionId) throws Exception {

		Question question = questionDao.findById(questionId);
		log.debug("question {}", question);
		List<Answer> answers = answerDao.findAllByQuestionId(questionId);

		ModelAndView mav = new ModelAndView("/qna/show");
		mav.addObject("question", question);
		mav.addObject("answers", answers);
		return mav;
	}

	// 2.CreateFormQuestionController
	@RequestMapping(value = "/qna/form", method = RequestMethod.GET)
	public ModelAndView createForm(HttpSession session) throws Exception {

		if (!UserSessionUtils.isLogined(session)) {
			ModelAndView mav = new ModelAndView("/user/login");
			return mav;
		}

		ModelAndView mav = new ModelAndView("/qna/form");
		return mav;
	}

	// 3.CreateQuestionController
	@RequestMapping(value = "/qna/create", method = RequestMethod.POST)
	public String createQuestion(HttpSession session, @RequestParam String title, @RequestParam String contents)
			throws Exception {

		if (!UserSessionUtils.isLogined(session)) {
			throw new IllegalStateException("로그인이 필요합니다");
		}

		User user = UserSessionUtils.getUserFromSession(session);
		Question question = new Question(user.getUserId(), title, contents);
		questionDao.insert(question);

		return "redirect:/";
	}

	// 4.UpdateFormQuestionController
	@RequestMapping(value = "/qna/updateForm", method = RequestMethod.GET)
	public ModelAndView updateFormQuestion(HttpSession session, @RequestParam long questionId) throws Exception {

		if (!UserSessionUtils.isLogined(session)) {
			ModelAndView mav = new ModelAndView("/user/login");
			return mav;
		}

		Question question = questionDao.findById(questionId);

		if (!question.isSameUser(UserSessionUtils.getUserFromSession(session))) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}

		ModelAndView mav = new ModelAndView("/qna/update");
		mav.addObject("question", question);

		return mav;
	}

	// 5.UpdateQuestionContorller
	@RequestMapping(value = "/qna/update", method = RequestMethod.POST)
	public String updateQuestion(HttpSession session, @RequestParam long questionId, @RequestParam String title,
			@RequestParam String contents) throws Exception {

		if (!UserSessionUtils.isLogined(session)) {
			// ModelAndView mav = new ModelAndView("/user/login");
			throw new IllegalStateException("로그인이 필요합니다");
		}

		Question question = questionDao.findById(questionId);

		if (!question.isSameUser(UserSessionUtils.getUserFromSession(session))) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}

		Question newQuestion = new Question(question.getWriter(), title, contents);
		question.update(newQuestion);
		questionDao.update(question);
		// ModelAndView mav = new ModelAndView("/index");
		// return mav;

		return "redirect:/";
	}

	// 6.DeleteQuestionController

	@RequestMapping(value = "/qna/delete", method = RequestMethod.POST)
	public String deleteQuestion(HttpSession session, @RequestParam long questionId) throws Exception {

		if (!UserSessionUtils.isLogined(session)) {
			// ModelAndView mav = new ModelAndView("/user/login");
			throw new IllegalStateException("로그인이 필요합니다");
		}

		Question question = questionDao.findById(questionId);

		if (!question.isSameUser(UserSessionUtils.getUserFromSession(session))) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 삭제할 수 없습니다.");
		}

		qnaService.deleteQuestion(questionId, UserSessionUtils.getUserFromSession(session));
		return "redirect:/";

	}
	// 7.AddAnswerController

	
	// @RequestMapping(value = "/{questionId}/answers/{answerId}", method =
	// RequestMethod.DELETE)
	// public Result deleteAnswer(HttpSession session, @PathVariable long
	// answerId) throws Exception {
	// if (!UserSessionUtils.isLogined(session)) {
	// return Result.fail("Login is required");
	// }
	//
	// Answer answer = answerDao.findById(answerId);
	// if (!answer.isSameUser(UserSessionUtils.getUserFromSession(session))) {
	// return Result.fail("다른 사용자가 쓴 글을 삭제할 수 없습니다.");
	// }
	//
	// try {
	// answerDao.delete(answerId);
	// return Result.ok();
	// } catch (DataAccessException e) {
	// return Result.fail(e.getMessage());
	// }
	// }
	//

	// 8.deleteAnswerController

//	@RequestMapping(value = "/api/qna/deleteAnswer", method = RequestMethod.DELETE)
//	public Result deleteAnswer(HttpSession session, @PathVariable long answerId) throws Exception {
//
//		if (!UserSessionUtils.isLogined(session)) {
//			return Result.fail("Login is required");
//		}
//		log.debug("test10");
//
//		// Answer answer = answerDao.findById(answerId);
//
//		// if (!answer.) {
//		// return Result.fail("다른 사용자가 쓴 글을 삭제할 수 없습니다.");
//		//// throw new IllegalStateException("로그인이 필요합니다.");
//		// }
//
//		try {
//			answerDao.delete(answerId);
//			return Result.ok();
//		} catch (DataAccessException e) {
//			return Result.fail(e.getMessage());
//		}

	}


