package fr.paris.lutece.plugins.releaser.util;

import java.io.File;
import java.util.Date;

import fr.paris.lutece.plugins.releaser.business.WorkflowReleaseContext;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ReleaserUtils
{

    public static final String REGEX_ID = "^[\\d]+$";
    public static String getWorklowContextDataKey(String strArtifactId,int nContextId)
    {
         return ConstanteUtils.CONSTANTE_RELEASE_CONTEXT_PREFIX+strArtifactId+"_"+nContextId;
    }

    
   
    public static String getLocalSitePath( String strSiteName )
    {
        String strCheckoutBasePath = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_LOCAL_SITE_BASE_PAH );

        return strCheckoutBasePath + File.separator + strSiteName;
    }
    
    public static String getLocalSitePomPath( String strSiteName  )
    {
        return getLocalSitePath( strSiteName ) + File.separator + ConstanteUtils.CONSTANTE_POM_XML;
    }

    public static String getLocalComponentPath( String strComponentName )
    {
        String strLocaleComponentBasePath = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_LOCAL_COMPONENT_BASE_PAH );

        return strLocaleComponentBasePath + File.separator + strComponentName;
    }

    public static String getLocalComponentPomPath( String strComponentName )
    {
        return getLocalComponentPath( strComponentName ) + File.separator + ConstanteUtils.CONSTANTE_POM_XML;
    }

    public static String getGitComponentName( String strScmDeveloperConnection )
    {

        if ( strScmDeveloperConnection.contains( "/" ) && strScmDeveloperConnection.contains( ".git" ) )
        {
            String [ ] tabDevConnection = strScmDeveloperConnection.split( "/" );
            return tabDevConnection [tabDevConnection.length - 1].replace( ".git", "" );
        }
        return null;
    }

    public static void addTechnicalError( CommandResult commandResult, String strError, Exception e ) throws AppException
    {

        if ( e != null )
        {
            AppLogService.error( strError, e );
        }
        else
        {
            AppLogService.error( strError );
        }

        if ( commandResult != null )
        {
            commandResult.setError( strError );
            commandResult.setStatus( CommandResult.STATUS_ERROR );
            commandResult.setRunning( false );
            commandResult.setErrorType( CommandResult.ERROR_TYPE_STOP );
            commandResult.setDateEnd( new Date( ));
        }
        if ( e != null )
        {
            throw new AppException( strError, e );
        }
        else
        {
            throw new AppException( strError );
        }
    }

    public static void addTechnicalError( CommandResult commandResult, String strError ) throws AppException
    {
        addTechnicalError( commandResult, strError, null );
    }

    public static void startCommandResult( WorkflowReleaseContext context )
    {
        CommandResult commandResult = new CommandResult( );
        commandResult.setDateBegin( new Date( ));
        commandResult.setLog( new StringBuffer( ) );
        commandResult.setRunning( true );
        commandResult.setStatus( CommandResult.STATUS_OK );
        commandResult.setProgressValue( 0 );
        context.setCommandResult( commandResult );

    }
    
    public static void logStartAction( WorkflowReleaseContext context,String strActionName )
    {
       
        context.getCommandResult( ).getLog( ).append( "******************Start Action: \""+strActionName +"\" *******************\n\r" );
   
    }

    public static void logEndAction( WorkflowReleaseContext context,String strActionName )
    {
        context.getCommandResult( ).getLog( ).append( "******************End Action:\"" +strActionName +"\" *******************\n\r" );
   
    }

    public static void stopCommandResult( WorkflowReleaseContext context )
    {
        context.getCommandResult( ).setRunning( false );
        context.getCommandResult( ).setDateEnd( new Date( ));
        context.getCommandResult( ).setProgressValue( 100 );
    }
    

    /**
     * convert a string to int
     * 
     * @param strParameter
     *            the string parameter to convert
     * @return the conversion
     */
    public static int convertStringToInt( String strParameter )
    {
        int nIdParameter = ConstanteUtils.CONSTANTE_ID_NULL;

        try
        {
            if ( ( strParameter != null ) && strParameter.matches( REGEX_ID ) )
            {
                nIdParameter = Integer.parseInt( strParameter );
            }
        }
        catch( NumberFormatException ne )
        {
            AppLogService.error( ne );
        }

        return nIdParameter;
    }


}
