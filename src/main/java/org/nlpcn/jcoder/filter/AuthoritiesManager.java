package org.nlpcn.jcoder.filter;

import org.nlpcn.jcoder.domain.Token;
import org.nlpcn.jcoder.run.mvc.view.JsonView;
import org.nlpcn.jcoder.service.TokenService;
import org.nlpcn.jcoder.util.ApiException;
import org.nlpcn.jcoder.util.Restful;
import org.nlpcn.jcoder.util.StringUtil;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;

import javax.servlet.http.HttpSession;

public class AuthoritiesManager implements ActionFilter {

	public static final String TOKEN = "token";

	@Override
	public View match(ActionContext actionContext) {
		HttpSession session = Mvcs.getHttpSession();

		String tokenStr = actionContext.getRequest().getHeader(TokenService.CLUSTER_HEAD);

		Object obj = session.getAttribute(TOKEN);

		if(obj!=null){
			return null ;
		}

		if (StringUtil.isNotBlank(tokenStr)) {
			try {
				Token token = TokenService.getToken(tokenStr);
				if (token != null) {
					actionContext.getRequest().getSession().setAttribute(TOKEN, token);
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		return new JsonView(Restful.instance().code(ApiException.TokenAuthorNotFound).msg("未登录").ok(false));

	}

}
