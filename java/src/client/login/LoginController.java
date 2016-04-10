package client.login;

import client.base.Controller;
import client.base.IAction;
import client.misc.IMessageView;
import client.services.ServerProxy;
import client.services.UserCookie;
import shared.dto.AuthDTO;


/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController {

	private IMessageView messageView;
	private IAction loginAction;
	
	/**
	 * LoginController constructor
	 * 
	 * @param view Login view
	 * @param messageView Message view (used to display error messages that occur
	 *                       during the login process)
	 */
	public LoginController(ILoginView view, IMessageView messageView) {
		super(view);
		this.messageView = messageView;
	}
	
	private ILoginView getLoginView() {
		return (ILoginView)super.getView();
	}

	/**
	 * Sets the action to be executed when the user logs in
	 * 
	 * @param value The action to be executed when the user logs in
	 */
	public void setLoginAction(IAction value) {
		loginAction = value;
	}

	@Override
	public void start() {
		getLoginView().showModal();
	}

	private void setLocalPlayerInfo() {
		UserCookie.getInstance().setName(getLoginView().getLoginUsername());
		UserCookie.getInstance().setId(UserCookie.getInstance().getPlayerId());
	}

	@Override
	public void signIn() {
		final String username = getLoginView().getLoginUsername();
		final String password = getLoginView().getLoginPassword();

		final AuthDTO dto = new AuthDTO(username, password);
		if(ServerProxy.getInstance().authenticateUser(dto)) {
			// If log in succeeded
			this.setLocalPlayerInfo();
			this.getLoginView().closeModal();
			loginAction.execute();
		} else {
			messageView.setTitle("Invalid Username");
			messageView.setMessage("Nice try.  You're not registered.");
			messageView.showModal();
		}
	}

	@Override
	public void register() {
		final String username = getLoginView().getRegisterUsername();
		final String password = getLoginView().getRegisterPassword();
		final String password2 = getLoginView().getRegisterPasswordRepeat();

		if (validateUsername(username) && validatePassword(password)) {
			if (!password.equals(password2)) {
				messageView.setTitle("Bad passwords");
				messageView.setMessage("Your passwords don't match.  Try again.");
				messageView.showModal();
			} else {
				final AuthDTO dto = new AuthDTO(username, password);
				if (ServerProxy.getInstance().registerUser(dto)) {
					// If register succeeded
					this.setLocalPlayerInfo();
					getLoginView().closeModal();
					loginAction.execute();
				} else {
					messageView.setTitle("Unable to register user");
					messageView.setMessage("Unable to register.  Please try again.");
					messageView.showModal();
				}
			}
		} else {
			messageView.setTitle("Invalid Credentials");
			messageView.setMessage("If the boxes are red, don't press the button. duh.");
			messageView.showModal();
		}

	}

	static boolean validateUsername(final String username) {
		final int MIN_UNAME_LENGTH = 3;
		final int MAX_UNAME_LENGTH = 7;

		if (username.length() < MIN_UNAME_LENGTH
				|| username.length() > MAX_UNAME_LENGTH) {
			return false;
		} else {
			for (char c : username.toCharArray()) {
				if (!Character.isLetterOrDigit(c)
						&& c != '_' && c != '-') {
					return false;
				}
			}
		}

		return true;
	}

	static boolean validatePassword(final String password) {
		final int MIN_PASS_LENGTH = 5;
		if (password.length() < MIN_PASS_LENGTH) {
			return false;
		} else {
			for (char c : password.toCharArray()) {
				if (!Character.isLetterOrDigit(c)
						&& c != '_' && c != '-') {
					return false;
				}
			}
		}

		return true;
	}
}
