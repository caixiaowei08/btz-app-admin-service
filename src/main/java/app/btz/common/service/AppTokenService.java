package app.btz.common.service;

import app.btz.common.authority.AuthorityPojo;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by User on 2017/6/17.
 */
public interface AppTokenService {

    public AuthorityPojo getAuthorityPojoByToken(HttpServletRequest request);

}
