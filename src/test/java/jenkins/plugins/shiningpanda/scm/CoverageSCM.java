/*
 * ShiningPanda plug-in for Jenkins
 * Copyright (C) 2011-2012 ShiningPanda S.A.S.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jenkins.plugins.shiningpanda.scm;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.scm.ChangeLogParser;
import hudson.scm.NullChangeLogParser;
import hudson.scm.PollingResult;
import hudson.scm.SCMDescriptor;
import hudson.scm.SCMRevisionState;
import hudson.scm.NullSCM;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class CoverageSCM extends NullSCM
{

    /**
     * List of the folders to create
     */
    private String[] htmlDirs;

    /**
     * Constructor using fields
     * 
     * @param htmlDirs
     *            The folders to create
     */
    public CoverageSCM(String... htmlDirs)
    {
        super();
        this.htmlDirs = htmlDirs;
    }

    public SCMRevisionState calcRevisionsFromBuild(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener)
            throws IOException, InterruptedException
    {
        return null;
    }

    protected PollingResult compareRemoteRevisionWith(@SuppressWarnings("rawtypes") AbstractProject project, Launcher launcher,
            FilePath workspace, TaskListener listener, SCMRevisionState baseline) throws IOException, InterruptedException
    {
        return PollingResult.NO_CHANGES;
    }

    public boolean checkout(AbstractBuild<?, ?> build, Launcher launcher, FilePath remoteDir, BuildListener listener,
            File changeLogFile) throws IOException, InterruptedException
    {
        for (String htmlDir : htmlDirs)
        {
            File htmlDirFile = new File(build.getWorkspace().getRemote(), htmlDir);
            htmlDirFile.mkdirs();
            FileUtils.writeStringToFile(new File(htmlDirFile, "coverage_html.js"), "");
            FileUtils.writeStringToFile(new File(htmlDirFile, "status.dat"), "");
            FileUtils.writeStringToFile(new File(htmlDirFile, "index.html"), "");
        }
        return createEmptyChangeLog(changeLogFile, listener, "log");
    }

    public ChangeLogParser createChangeLogParser()
    {
        return new NullChangeLogParser();
    }

    @Extension
    public static class DescriptorImpl extends SCMDescriptor<NullSCM>
    {
        public DescriptorImpl()
        {
            super(null);
        }

        public String getDisplayName()
        {
            return getClass().getName();
        }
    }
}