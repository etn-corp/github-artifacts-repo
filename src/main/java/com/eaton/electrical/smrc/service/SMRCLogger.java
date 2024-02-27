package com.eaton.electrical.smrc.service;

import org.apache.log4j.*;

/**
 * @author e0062722
 *
 */
public class SMRCLogger {

        private static Logger logger = Logger.getLogger("SMRCLogger");

        public static final int DEBUG = 0;
        public static final int INFO = 1;
        public static final int WARN = 2;
        public static final int ERROR = 3;
        public static final int FATAL = 4;

        public static void refreshConfig() {
        }

        public static void log( int prio, String msg ) {
            switch ( prio ) {
                case INFO:
                    logger.info( msg );
                    break;
                case WARN:
                    logger.warn( msg );
                    break;
                case ERROR:
                    logger.error( msg );
                    break;
                case FATAL:
                    logger.fatal( msg );
                    break;
                default:
                    logger.debug( msg );
            }
        }

        public static void info( Object msg ) {
            logger.info( msg );
        }

        public static void info( Object msg, Throwable t ) {
            logger.info( msg, t );
        }

        public static void warn( Object msg ) {
            logger.warn( msg );
        }

        public static void warn( Object msg, Throwable t ) {
            logger.warn( msg, t );
        }

        public static void error( Object msg ) {
            logger.error( msg );
        }

        public static void error( Object msg, Throwable t ) {
            logger.error( msg, t );
        }

        public static void fatal( Object msg ) {
            logger.fatal( msg );
        }

        public static void fatal( Object msg, Throwable t ) {
            logger.fatal( msg, t );
        }

        public static void debug( Object msg ) {
            logger.debug( msg );
        }
        public static void debug( Object msg, Throwable t ) {
            logger.debug( msg, t );
        }
        
        public static boolean isDebuggerEnabled() {        	
        	return logger.isDebugEnabled();        	
        }
} //class
