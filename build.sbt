scalaVersion := "2.11.8"

enablePlugins(AndroidApp)
enablePlugins(AndroidProtify)
useSupportVectors

versionCode := Some(1)
version := "0.1-SNAPSHOT"

instrumentTestRunner :=
  "android.support.test.runner.AndroidJUnitRunner"

platformTarget := "android-25"
minSdkVersion := "21"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: "-encoding" :: "utf-8" :: Nil
scalacOptions += "-Xexperimental"

libraryDependencies ++= (
  "com.android.support"                               %  "appcompat-v7"         % "24.0.0" ::
  "com.android.support"                               %  "design"               % "24.0.0" ::
  "com.android.support"                               %  "multidex"             % "1.0.1"  ::
  "com.wrapp.floatlabelededittext"                    %  "library"              % "0.0.6"  ::
  "com.ikimuhendis"                                   %  "ldrawer"              % "0.1"    ::
  "com.getbase"                                       %  "floatingactionbutton" % "1.10.1" ::
  "com.github.dmytrodanylyk.circular-progress-button" %  "library"              % "1.1.3"  ::
  "com.google.code.gson"                              %  "gson"                 % "2.7"    ::
  "org.scalatest"                                     %% "scalatest"            % "3.0.0"  % "test"        ::
  "junit"                                             %  "junit"                % "4.12"   % "test"        ::
  "com.android.support.test"                          %  "runner"               % "0.5"    % "androidTest" ::
  "com.android.support.test.espresso"                 %  "espresso-core"        % "2.2.2"  % "androidTest" ::
  Nil).map(_.exclude("com.android.support", "support-v4")) :+ aar("com.android.support" % "support-v4" % "24.0.0")

