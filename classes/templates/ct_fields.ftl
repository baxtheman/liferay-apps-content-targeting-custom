<#assign aui = PortletJspTagLibs["/META-INF/aui.tld"] />
<#assign liferay_ui = PortletJspTagLibs["/META-INF/liferay-ui.tld"] />
<#assign portlet = PortletJspTagLibs["/META-INF/liferay-portlet.tld"] />

<@aui["input"] inlineField=true name="refererSubstring" label="Substring contained into referer header" value=refererSubstring>
	<@aui["validator"] name="required">
	</@>
</@>

<div>
<small>made by <a href="https://twitter.com/@baxtheman">@baxtheman</a></small>
</div>

