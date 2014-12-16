NOTE this project will remain quite buggy for some time (2018 or so)
as parsing CSS files is fairly complex work.
If it doesn't do something you think it should, try to find a work around
or collaborate with us to fix the issue (i.e. submit a issue, tests4j test case,
and patch).  


This project is part of the 'externalized css presentation' pattern.   This pattern uses the 
css file to drive the GWT application, so that  a css file may be changed after a GWT application
is compiled, to customize the way a application looks.
     This is the opposite of GWTs ClientBundle approach  which 'drives' the GWT application style sheets 
from the GWT source code/xml files at compile time.
 
When selecting which way to approach css is your applicaation here are some pros and cons of this approach
vs the ClientBundle approach;
	Note that the two approaches could be used together in a single application (i.e.);
  	The login presentation of a GWT app has css done with the ClientBundle, but
after a successful login the gwt_css.adligo.org approach is used.

gwt_css.adligo.org approach               
Pros    
  1) css can be altered by a web master after a deployment with out 
the need for knowlege of gwt or java.
  2) Style sheets can be programmatically parsed to fix issues in browsers i.e.;
  OmniWeb donsn't seem to know how to render .png files with transparency 
  over a background-color using a background-image style, 
  but it can be done with a GWT absolute panel,
  which contains a Image.  A style sheet can contain the image url,
  which the application can parse to figure out which background image
  should show.
  3) Arbitrary non style sheet information can be passed to the application
  using the style sheet i.e.;
  A application needs to know how long to wait before terminating a
  authenticate users session.  This information can be added to the 
  style sheet;
  .autoLogoutTime {
  	myapp-logout-time: 5;
  }
  The application can pick up this information by parsing the style sheet.  
  This can allow web masters to control web applications using a file 
  external to the application, which the application is already aware of.  
  2) It is backward compatible with regular development of style sheets
  (i.e. the GwtDesigner style sheet GUI), in a way that non GWT developers
  are more familiar with.
  
Cons
  1) The css file is a little more bulky/slower to download.
  2) The application has to process the css file using java script,
  which is additional overhead.
  3) Applications that require different css for each browser need to
have a new css file for each browser and direct the browser
to the css file.  
  
ClientBundle approach
Pros
  1) The css is split up by browser by the compile, which is less work for the developer.
        Note that browser detection/css file location could be added to this 
     project to reduce some of that work, also I have generally found
     that css specific to particular browsers can generally be done 
     in a mutually exclusive way.  As far as I can tell css differences
     were mostly a carry over from the browser war era, where InternetExplorer
     did one thing and Netscape did something else.  
     	However, css interpretations by different browsers which could cause
     differences in page rendering are hopefully always handled
     correctly by the GWT compiler.  If there is a bug in the GWT
     compiler rendering of the css files, a recompile (possibly involving
     a patched version of GWT) could be required.
      
  2) The css is obsfucated which creates a smaller(less bytes) css file,
  also this is probably a bit more secure as hackers will have a harder time
  correlating javascript to what they see in the browser window.  
     Note that a hacker could just as easily look for server calls 
   RPC, WebSocket, etc. and skip the view layer altogether, when 
   trying to attack a system. 
Cons
  1) Changes to the application styles require a development,
  build and release iteration to make a change to the style sheet.
  This could delay the change for a considerable time, since
  most iteration cycles are at least a week for large projects.

  
Other notes;
   Standard Adligo matrix modeling conventions have be altered a bit
   in this project, so interfaces don't only depend on interfaces and
   JSE classes.  This was done because there will probably never 
   be another implementation of these models, where as standard Adligo
   matrix modeling assumes there may be other implementations.