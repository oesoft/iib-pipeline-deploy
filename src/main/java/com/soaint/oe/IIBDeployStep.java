/*
 * The MIT License
 *
 * Copyright (c) 2013-2014, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.soaint.oe;

import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.jenkinsci.plugins.durabletask.BourneShellScript;
import org.jenkinsci.plugins.durabletask.DurableTask;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.durable_task.DurableTaskStep;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.Extension;

public final class IIBDeployStep extends DurableTaskStep {

	private String brokerFileName;
	private String integrationServerName;
	private String BARFileName;
	private String IIBPath;
	private StepContext contexto;

	@DataBoundConstructor
	public IIBDeployStep(String brokerFileName,String integrationServerName, String BARFileName) {
		if (brokerFileName == null && integrationServerName ==null && BARFileName==null)
			throw new IllegalArgumentException();
		this.brokerFileName = brokerFileName;
		this.integrationServerName = integrationServerName;
		this.BARFileName = BARFileName;
	}


	public String getIIBPath() {
		return IIBPath;
	}

	@DataBoundSetter
	public void setIIBPath(String IIBPath) {
		this.IIBPath = IIBPath;
	}

	@Override
	protected DurableTask task() {
		return new BourneShellScript(IIBPath+"/mqsideploy -n "+brokerFileName+" -e "+integrationServerName+" -a "+BARFileName+" -m");

	}

	@Override
	public StepExecution start(StepContext context) throws Exception {
		this.contexto = context;
		return super.start(context);
	}

	@Extension
	public static final class DescriptorImpl extends DurableTaskStepDescriptor {

		@Override
		public String getDisplayName() {
			return "IIB Deploy";
		}

		@Override
		public String getFunctionName() {
			return "iibDeploy";
		}

		@CheckForNull
		@Override
		public String argumentsToString(@Nonnull Map<String, Object> namedArgs) {
			if (namedArgs.containsKey("IIBPath")) {
				return (String) namedArgs.get("IIBPath");
			}
			return null;
		}

	}
	
	public String getBrokerFileName() {
		return brokerFileName;
	}



	public void setBrokerFileName(String brokerFileName) {
		this.brokerFileName = brokerFileName;
	}



	public String getIntegrationServerName() {
		return integrationServerName;
	}



	public void setIntegrationServerName(String integrationServerName) {
		this.integrationServerName = integrationServerName;
	}



	public String getBARFileName() {
		return BARFileName;
	}



	public void setBARFileName(String bARFileName) {
		BARFileName = bARFileName;
	}

}
