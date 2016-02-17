package client.login;

import client.base.*;
import client.misc.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import client.services.ServerProxy;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
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
	 * @param messageView Message view (used to display error messages that occur during the login process)
	 */
	public LoginController(ILoginView view, IMessageView messageView) {
		super(view);
		this.messageView = messageView;
	}
	
	public ILoginView getLoginView() {
		return (ILoginView)super.getView();
	}
	
	public IMessageView getMessageView() {
		return messageView;
	}
	
	/**
	 * Sets the action to be executed when the user logs in
	 * 
	 * @param value The action to be executed when the user logs in
	 */
	public void setLoginAction(IAction value) {
		loginAction = value;
	}
	
	/**
	 * Returns the action to be executed when the user logs in
	 * 
	 * @return The action to be executed when the user logs in
	 */
	public IAction getLoginAction() {
		return loginAction;
	}

	@Override
	public void start() {
		getLoginView().showModal();
	}

	// TODO: add proper error messaging
	@Override
	public void signIn() {
		final String username = getLoginView().getLoginUsername();
		final String password = getLoginView().getLoginPassword();

		final AuthDTO dto = new AuthDTO(username, password);
		if(ServerProxy.getInstance().authenticateUser(dto)) {
			// If log in succeeded
			getLoginView().closeModal();
			loginAction.execute();
		}
	}

	// TODO: add proper error messaging
	@Override
	public void register() {
		final String username = getLoginView().getRegisterUsername();
		final String password = getLoginView().getRegisterPassword();

		final AuthDTO dto = new AuthDTO(username, password);
		if(ServerProxy.getInstance().registerUser(dto)) {
			// If register succeeded
			getLoginView().closeModal();
			loginAction.execute();
		}
	}
}
