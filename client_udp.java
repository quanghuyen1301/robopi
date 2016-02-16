/******************************************************************************/
/*                                                                            */
/*                                                  FILE: DatagramClient.java */
/*                                                                            */
/*  Demonstrates a simple datagram client                                     */
/*  =====================================                                     */
/*                                                                            */
/*  V1.00   16-DEC-1998 Te                                                    */
/*  V1.10   12-OCT-2009 Te Cleaned up and extended                            */
/*                                                                            */
/******************************************************************************/

import java.net.* ;

/**
 *  A simple datagram client
 *  Shows how to send and receive UDP packets in Java
 *
 *  @author  P. Tellenbach,  http://www.heimetli.ch
 *  @version V1.00
 */
public class client_udp
{
   private final static int PACKETSIZE = 100 ;

   public static void main( String args[] )
   {
      // Check the arguments
      if( args.length != 2 )
      {
         System.out.println( "usage: java DatagramClient host port" ) ;
         return ;
      }

      DatagramSocket socket = null ;

      try
      {
         // Convert the arguments first, to ensure that they are valid
         InetAddress host = InetAddress.getByName( args[0] ) ;
         int port         = Integer.parseInt( args[1] ) ;

         // Construct the socket
         socket = new DatagramSocket() ;

         // Construct the datagram packet
         byte [] data = "up".getBytes() ;
         DatagramPacket packet = new DatagramPacket( data, data.length, host, port ) ;

         // Send it
         socket.send( packet ) ;

         // Set a receive timeout, 2000 milliseconds
         socket.setSoTimeout( 2000 ) ;

         // Prepare the packet for receive
         packet.setData( new byte[PACKETSIZE] ) ;

         // Wait for a response from the server
         socket.receive( packet ) ;

         // Print the response
         System.out.println( new String(packet.getData()) ) ;

      }
      catch( Exception e )
      {
         System.out.println( e ) ;
      }
      finally
      {
         if( socket != null )
            socket.close() ;
      }
   }
}

