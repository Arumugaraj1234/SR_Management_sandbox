package com.vmfg.general.response;

import java.util.List;

import com.vmfg.authentication.LinkUrlEntity;
import com.vmfg.authentication.UIModuleMst;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMenuByUserRoleResponse {
	List<UIModuleMst> uiModule;
	String landingPageUrl;
	String currency;
	List<LinkUrlEntity>authUrl;

}
