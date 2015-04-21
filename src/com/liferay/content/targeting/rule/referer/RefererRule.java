/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.content.targeting.rule.referer;

import com.liferay.content.targeting.anonymous.users.model.AnonymousUser;
import com.liferay.content.targeting.api.model.BaseRule;
import com.liferay.content.targeting.api.model.Rule;
import com.liferay.content.targeting.model.RuleInstance;
import com.liferay.content.targeting.rule.categories.SampleRuleCategory;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Daniele Baggio, @baxtheman
 */
@Component(immediate = true, service = Rule.class)
public class RefererRule extends BaseRule {

	@Activate
	@Override
	public void activate() {
		super.activate();
	}

	@Deactivate
	@Override
	public void deActivate() {
		super.deActivate();
	}

	/**
	* test the rule with request & user context
	*/
	@Override
	public boolean evaluate(
			HttpServletRequest request, RuleInstance ruleInstance,
			AnonymousUser anonymousUser)
		throws Exception {

		String referer = request.getHeader(HTTP_REFERER);

		if (Validator.isNull(referer)) {

			return false;
		}

		JSONObject jsonObj = JSONFactoryUtil.createJSONObject(
			ruleInstance.getTypeSettings());

		String refererSubstring = jsonObj.getString(REFERER_FIELD);

		if (Validator.isNull(refererSubstring)) {

			return false;
		}

		return referer.contains(refererSubstring);	
	}

	@Override
	public String getIcon() {
		return "icon-exchange";
	}

	@Override
	public String getRuleCategoryKey() {
		return SampleRuleCategory.KEY;
	}

	@Override	
	public String getSummary(RuleInstance ruleInstance, Locale locale) {

		String summary = StringPool.BLANK;

		try {
			JSONObject jsonObj = JSONFactoryUtil.createJSONObject(
				ruleInstance.getTypeSettings());

			String refererSubstring = jsonObj.getString(REFERER_FIELD);

			summary = LanguageUtil.format(
				locale, "summary-referer-regex-x", refererSubstring);
		}
		catch (JSONException jse) {
		}

		return summary;
	}

	@Override
	public String processRule(
		PortletRequest request, PortletResponse response, String id,
		Map<String, String> values) {

		String refererSubstring = GetterUtil.getString(
			values.get(REFERER_FIELD));

		JSONObject jsonObj = JSONFactoryUtil.createJSONObject();

		jsonObj.put(REFERER_FIELD, refererSubstring);

		return jsonObj.toString();
	}

	@Override
	protected void populateContext(
		RuleInstance ruleInstance, Map<String, Object> context,
		Map<String, String> values) {

		String refererSubstring = StringPool.BLANK;

		if (!values.isEmpty()) {

			refererSubstring = GetterUtil.getString(values.get(REFERER_FIELD));
		}
		else if (ruleInstance != null) {

			try {

				JSONObject jsonObj = JSONFactoryUtil.createJSONObject(
					ruleInstance.getTypeSettings());

				refererSubstring = jsonObj.getString(REFERER_FIELD);
			}
			catch (JSONException jse) {
			}
		}

		context.put(REFERER_FIELD, refererSubstring);
	}

 	private static final String HTTP_REFERER = "Referer";
	private static String REFERER_FIELD = "refererSubstring";
}