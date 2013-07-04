MuseumAppAndroid
================

Museum “Experience” System / Application - Android 

This is a temporary readme file


CourseWork N – Enterprise Java Programming Eoin Butler 07502621 e.butler6@nugialway.ie The application runs with two Java files “Cleint.java” and “Server.java” as shown below. It is a basic Client/Server application using sockets. Initially I planned on having the server work with a remote database (MS ACCESS) for storing and retrieving system records.
The Client application uses the following
 JLabel
 JTextField
 JButton
 JPanel
 JTextArea
 JTable
 Layout Managers
 Dialogue
The Server application uses the following
 JButton
 JPanel
 JTextArea
 RandomAccessFile
 Layout Managers
We use server sockets to computers to connect with each other.
ODBC PROBLEM – During my design I found several difficulties in applying a local system DNS db to work with my application. After hours of attempting to resolve the issue I found that it was my computers x64 bit architecture that would not allow me to run the server with my local database. In order to run the application I had to change the windows 7 ODBC location to the original x32 bit operator. Had this have worked the application would have been responsive with a simple MS Access database.
Below is a list of the methods in my “Client”  Client() - this is the class constructor used for initialization.  setUp() - this method is used to setup and allocate the GUI objects.
TCP/IP
Server
Client
 connect() - this method is used to connect to the socket.
 run() - this method is used to create input/output streams and to listen for
input from the socket.
 closeConnection() - is used to close the socket connection and the input/output
streams.
 sendData(String ) - is used to send data to the server.
 main() - this is the entry point for the client application.
Below is a list of the methods in my “Server”
 tcpServer() - this is the class constructor used for initialization.
 setUp() - this method is used to setup and allocate the GUI objects.
 run() - this method is used to create the server socket, input/output streams and
to listen for input from the socket.
 closeConnection() - is used to close the socket connection and the input/output
streams.
 findName()* - this method is a stub in this version.
 addName()* - this method adds a name using the RandomAccessFile API and
the record ID.
 updateName()* - this method updates a name using the RandomAccessFile API and
the record ID.
 deleteName()* - this method will use the record ID, using the RandomAccessFile
API to delete a record in the file.
 sendData(String ) - is used to send data to the client.
 main() - this is the entry point for the server application.
the methods denoted with an * should have had direct relations with the MS Access db.
SQL
In order to send the input to the db it must be converted to SQL. Below is a quick
explination of how this should have ideally worked.
Database Name: IssueTraker1
Database Table: trackerRecords
Database fields: TechName, CustName, IssueDetails, Urgency, Status
try {
// creates an object that implements the Statement interface
Statement statement = connect.createStatement();
if ( tokens.countTokens() >= 1 ) {
while( tokens.hasMoreTokens() ) {
messageTokens[ ii ] = tokens.nextToken().trim().toString() ;
display.append("\n" + messageTokens[ ii ] ) ;
ii++ ;
}
}
display.append( "\nThe size of the recID is " +
messageTokens[ 1 ].length() );
String query = "INSERT INTO trackerRecords (" +
"TechName, CustName, IssueDetails, Urgency, " +
"Status” +
") VALUES ('" +
messageTokens[ 1 ] + "', '" +
messageTokens[ 2 ] + "', '" +
messageTokens[ 3 ] + "', '" + messageTokens[ 4 ] + "', '" + messageTokens[ 5 ]+ "')"; int result = statement.executeUpdate( query ); if ( result == 1 ) display.append("\nThe record was successful in addName()." ) ; statement.close(); } catch ( SQLException sqlex ) { sqlex.printStackTrace(); display.append( sqlex.toString() ); }
1. Statement statement  Used for executing a static SQL statement and obtaining the results produced by it. 2. executeQuery(String sql)  Executes a SQL statement that returns a single ResultSet. 3. String query  Contains the SQL query statement. This new code adds a record to the IssueTracker1 database. The following code updates a record in the IssueTracker1 database. String query = "UPDATE trackerRecords SET " + "TechName='" + messageTokens[ 2 ] + "', CustName='" + messageTokens[ 3 ] + "', IssueDetails='" + messageTokens[ 4 ] + "', Urgency='" + messageTokens[ 5 ] + "', Status='" + messageTokens[ 6 ] + "' WHERE id=" + messageTokens[ 1 ]; int result = statement.executeUpdate( query ); if ( result == 1 ) display.append("\nThe record was successful in updateName()." ) ; The following code deletes a record in the IssueTracker1 database. String query = "DELETE FROM trackerRecords WHERE id=" + messageTokens[ 1 ]; int result = statement.executeUpdate( query ); if ( result == 1 ) display.append("\nThe record was successful in deleteName()." ) ;
JAVA FILES
Server.java
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.sql.*;
public class Server extends JFrame implements ActionListener {
private int port = 5050;
private boolean foundRec = false;
private String url;
private String messageTokens[] = new String[16],
addrRecord[][] = {
{ "Eoin Butler", "Mary Smith", "Jammed Disk Tray", "5", "SOLVED" },
};
private ServerSocket serverSocket;
private BufferedReader input;
private PrintWriter output;
private Container c;
private JTextArea display;
private JButton cancel, send, exit;
private JPanel buttonPanel;
private StringTokenizer tokens;
private File aFile;
private RandomAccessFile file;
private String pData[][] = new String[300][11];
private Connection connect;
private String query;
private boolean myDebug = true;
private Statement statement;
public Server() {
super("Server");
setup();
run();
}
// Initialize the db
public void setup() {
c = getContentPane();
exit = new JButton("Exit");
exit.setBackground(Color.red);
exit.setForeground(Color.white);
buttonPanel = new JPanel();
buttonPanel.add(exit);
c.add(buttonPanel, BorderLayout.SOUTH);
exit.addActionListener(this);
display = new JTextArea();
display.setEditable(false);
addWindowListener(new WindowHandler(this));
c.add(new JScrollPane(display), BorderLayout.CENTER);
setSize(400, 700);
setLocation(10, 20);
show();
}
public void InitRecord() {
ResultSet rs;
sysPrint("\nIn InitRecord() method.");
try {
url = "jdbc:odbc:IssueTracker1";
Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
connect = DriverManager.getConnection(url);
sysPrint("\nThe value of connect is " + connect);
statement = connect.createStatement();
if (connect == null) {
display.append("Connection... was not successful\n");
JOptionPane.showMessageDialog(null,
"The IssueTracker1 was not found. \n",
"Database not found", JOptionPane.INFORMATION_MESSAGE);
} else {
sysPrint("\nReady to initialize database.");
statement = connect.createStatement();
query = "SELECT * FROM trackerRecords ";
rs = statement.executeQuery(query);
if (!rs.next()) {
for (int ii = 0; ii < addrRecord.length; ii++) {
query = "INSERT INTO trackerRecords ("
+ "Techname, Custname, IssueDetails, Urgency, "
+ "Status"
+ ") VALUES ('" + addrRecord[ii][0] + "', '"
+ addrRecord[ii][1] + "', '"
+ addrRecord[ii][2] + "', '"
+ addrRecord[ii][3] + "', '"
+ addrRecord[ii][4] + "', '"
+ addrRecord[ii][5] + "')";
int result = statement.executeUpdate(query);
if (result == 1)
display.append("\nThe record was successful in init().");
display.append("\nThe init record written was "
+ addrRecord[ii][0] + addrRecord[ii][1]
+ addrRecord[ii][2] + addrRecord[ii][3]
+ addrRecord[ii][4] + addrRecord[ii][5]);
}
}
}
statement.close();
} catch (ClassNotFoundException cnfex) {
// process ClassNotFoundExceptions here
cnfex.printStackTrace();
display.append("Connection unsuccessful\n" + cnfex.toString());
} catch (SQLException sqlex) {
// process SQLExceptions here
sqlex.printStackTrace();
display.append("Connection unsuccessful\n" + sqlex.toString());
} catch (Exception ex) {
// process remaining Exceptions here
ex.printStackTrace();
display.append(ex.toString());
}
}
// Method reads and writes data to the server
public void run() {
int i3 = 0;
try {
serverSocket = new ServerSocket(5050, 100,
InetAddress.getByName("127.0.0.1"));
display.setText("Server waiting for client on port "
+ serverSocket.getLocalPort() + "\n");
// server infinite loop
while (true) {
Socket socket = serverSocket.accept();
display.append("New connection accepted "
+ socket.getInetAddress() + ":" + socket.getPort()
+ "\n");
input = new BufferedReader(new InputStreamReader(
socket.getInputStream()));
output = new PrintWriter(socket.getOutputStream(), true);
InitRecord();
String message = "";
// print received data
try {
while (!message.toUpperCase().equals("QUIT")) {
message = (String) input.readLine();
tokens = new StringTokenizer(message);
if (tokens.countTokens() >= 1) {
int ii = 0;
while (tokens.hasMoreTokens()) {
messageTokens[ii] = tokens.nextToken()
.toString();
display.append("\n" + messageTokens[ii]);
ii++;
}
display.append("\nThe value of messageTokens[ 0 ] is "
+ messageTokens[0] + "\n");
if (messageTokens[0].toUpperCase().equals("FIND")) {
;
} else if (messageTokens[0].toUpperCase().equals(
"LISTALL")) {
display.append("\nCurrrently in run() method and if LISTALL construct.");
listNames();
} else if (messageTokens[0].toUpperCase().equals(
"ADD;;")) {
display.append("\nCurrrently in run() method and if add construct.");
addName(message);
} else if (messageTokens[0].toUpperCase().equals(
"UPDATE;;")) {
display.append("\nCurrrently in run() method and if update construct.");
updateName(message);
} else if (messageTokens[0].toUpperCase().equals(
"DELETE;;")) {
display.append("\nCurrrently in run() method and if delete construct.");
deleteName();
}
ii = 0;
} else {
display.append(message);
message = null;
break;
}
}
sendData("FROM SERVER==> QUIT");
} catch (IOException e) {
display.append("\n" + e);
}
// connection closed by client
try {
socket.close();
display.append("\n Connection closed by client");
} catch (IOException e) {
display.append("\n" + e);
}
}
} catch (IOException e) {
display.append("\n" + e);
}
}
// Exit Button action
public void actionPerformed(ActionEvent e) {
if (e.getSource() == exit)
closeConnection();
}
// Closes the Socket
private void closeConnection() {
try {
serverSocket.close();
input.close();
System.exit(0);
} catch (IOException e) {
display.append("\n" + e);
System.exit(0);
}
try {
connect.close();
} catch (SQLException sqlex) {
System.err.println("Unable to disconnect");
sqlex.printStackTrace();
}
}
// Print Notes
public void sysPrint(String str) {
if (myDebug) {
System.out.println(str);
}
}
// Add a name to the db
public void addName(String message) {
int ii = 0;
display.append("\nCurrrently in addName() method.");
tokens = new StringTokenizer(message, ";;");
try {
statement = connect.createStatement();
if (tokens.countTokens() >= 1) {
while (tokens.hasMoreTokens()) {
messageTokens[ii] = tokens.nextToken().trim().toString();
display.append("\n" + messageTokens[ii]);
ii++;
}
}
display.append("\nThe size of the recID is "
+ messageTokens[1].length());
String query = "INSERT INTO trackerRecords ("
+ "Techname, Custname, IssueDetails, Urgency, "
+ "Status" + ") VALUES ('"
+ messageTokens[1] + "', '" + messageTokens[2] + "', '"
+ messageTokens[3] + "', '" + messageTokens[4] + "', '"
+ messageTokens[5] + "')";
int result = statement.executeUpdate(query);
if (result == 1)
display.append("\nThe record was successful in addName().");
statement.close();
} catch (SQLException sqlex) {
sqlex.printStackTrace();
display.append(sqlex.toString());
}
display.append("\nThe record to be added is " + messageTokens[0] + " "
+ messageTokens[1] + " " + messageTokens[2] + " "
+ messageTokens[3] + " " + messageTokens[4] + " "
+ messageTokens[5]);
}
// Method lists names in the db
public void listNames() {
int ii = 0, iii = 0, recLength = addrRecord.length, numEntries = 0;
double loopLimit = 0;
String str = "";
Vector rows = new Vector();
ResultSet rs;
ResultSetMetaData rsmd;
foundRec = false;
sysPrint("In listNames() method.");
sendData("ListAll;;");
query = "SELECT * FROM trackerRecords ";
try {
sysPrint("The value of connect is " + connect);
Statement statement = connect.createStatement();
rs = statement.executeQuery(query);
while (rs.next()) {
pData[iii][0] = String.valueOf(rs.getInt(1));
pData[iii][1] = rs.getString(2);
pData[iii][2] = rs.getString(3);
pData[iii][3] = rs.getString(4);
pData[iii][4] = rs.getString(5);
pData[iii][5] = rs.getString(6);
display.append("\n" + pData[iii][0] + " " + pData[iii][1] + " "
+ pData[iii][2] + " " + pData[iii][3] + " "
+ pData[iii][4] + " " + pData[iii][5]);
// Begin at 0(iii) and pack data in locations
str = "" + (pData[iii][0]).trim() + ";; "
+ (pData[iii][1]).trim() + ";; "
+ (pData[iii][2]).trim() + ";; "
+ (pData[iii][3]).trim() + ";; "
+ (pData[iii][4]).trim() + ";; "
+ (pData[iii][5]).trim() + ";; " + ";;;";
sendData(str);
display.append("\nsendData " + str); // setting up to send the
// entire file
str = "";
iii++;
ii++;
}
statement.close();
} catch (SQLException sqlex) {
sqlex.printStackTrace();
display.append(sqlex.toString());
}
sendData("GetAllDone;;");
}
// Update a name in the db
public void updateName(String message) {
int ii = 0;
display.append("\nCurrrently in updateName() method.");
tokens = new StringTokenizer(message, ";;");
try {
statement = connect.createStatement();
if (tokens.countTokens() >= 1) {
while (tokens.hasMoreTokens()) {
messageTokens[ii] = tokens.nextToken().trim().toString();
display.append("\n" + messageTokens[ii]);
ii++;
}
}
display.append("\nThe size of the recID is "
+ messageTokens[1].length());
String query = "UPDATE addresses SET " + "Techname='"
+ messageTokens[2] + "', Custname='" + messageTokens[3]
+ "', IssueDetails='" + messageTokens[4] + "', Urgency='"
+ messageTokens[5] + "', Status='" + messageTokens[6]
+ "' WHERE id=" + messageTokens[1];
int result = statement.executeUpdate(query);
if (result == 1)
display.append("\nThe record was successful in updateName().");
statement.close();
} catch (SQLException sqlex) {
sqlex.printStackTrace();
display.append(sqlex.toString());
}
display.append("\nThe record to be updated is " + messageTokens[1]
+ " " + messageTokens[2] + " " + messageTokens[3] + " "
+ messageTokens[4] + " " + messageTokens[5] + " "
+ messageTokens[6]);
}
// Delete a name in the db
public void deleteName() {
int ii = 0, recLength = addrRecord.length;
display.append("\nCurrrently in deleteName() method.");
ii = messageTokens[1].indexOf(";");
messageTokens[1] = (messageTokens[1]).substring(0, ii);
try {
statement = connect.createStatement();
String query = "DELETE FROM trackerRecords WHERE id=" + messageTokens[1];
int result = statement.executeUpdate(query);
if (result == 1)
display.append("\nThe record was successful in deleteName().");
statement.close();
} catch (SQLException sqlex) {
sqlex.printStackTrace();
display.append(sqlex.toString());
}
display.append("\nThe record to be deleted was " + messageTokens[1]);
}
private void sendData(String str) {
output.println(str);
output.flush();
}
public static void main(String args[]) {
final Server server = new Server();
server.addWindowListener(new WindowAdapter() {
public void windowClosing(WindowEvent e) {
server.closeConnection();
System.exit(0);
}
});
}
// Closing the Socket
class WindowHandler extends WindowAdapter {
Server tcpS;
public WindowHandler(Server t) {
tcpS = t;
}
public void windowClosing(WindowEvent e) {
tcpS.closeConnection();
}
}
}
Client.java
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
public class Client extends JFrame implements ActionListener {
private int port = 5050;
private String server = "localhost";
private Socket socket = null;
private BufferedReader input;
private PrintWriter output;
private int ERROR = 1, iii = 0, numEntries = 0;
private Container c;
private JTextArea display;
private JButton clear, status, refresh, add, update, delete, exit;
private JPanel buttonPanel, textPanel, deckPanel, displayPanel, listPanel,
addCard, updateCard, deleteCard, findCard;
private CardLayout cardManager;
private BorderLayout borderManager;
private JTextField enterBox;
private JLabel enterLabel;
private StringTokenizer tokens;
private String messageTokens[] = new String[16];
private String pData[][] = new String[300][11];
private boolean myDebug = true, initPass = true;
private JTable table;
private String columnNames[] = { "Record ID", "TechName", "CustName",
"IssueDetails", "Urgency", "Status" };
private StatusDisp statusDisp;
private UpdateRec updateRec;
private NewRec newRec;
private DeleteRec deleteRec;
Client tcpC;
private JScrollPane scrollpane;
public Client() {
super("Client");
setUp();
connect();
run();
closeConnection();
}
// Initialise the application
private void setUp() {
c = getContentPane();
status = new JButton("Status");
refresh = new JButton("Refresh");
add = new JButton("Add");
update = new JButton("Update");
delete = new JButton("Delete");
;
exit = new JButton("Exit");
refresh.setBackground(Color.blue);
refresh.setForeground(Color.white);
status.setBackground(Color.blue);
status.setForeground(Color.white);
add.setBackground(Color.blue);
add.setForeground(Color.white);
update.setBackground(Color.blue);
update.setForeground(Color.white);
delete.setBackground(Color.blue);
delete.setForeground(Color.white);
exit.setBackground(Color.red);
exit.setForeground(Color.white);
buttonPanel = new JPanel();
buttonPanel.add(refresh);
buttonPanel.add(status);
buttonPanel.add(add);
buttonPanel.add(update);
buttonPanel.add(delete);
buttonPanel.add(exit);
c.add(buttonPanel, BorderLayout.SOUTH);
enterLabel = new JLabel(
"Enter a last name below and then press a button.");
enterLabel.setFont(new Font("Serif", Font.BOLD, 14));
enterLabel.setForeground(Color.black);
enterBox = new JTextField(100);
enterBox.setEditable(true);
textPanel = new JPanel();
textPanel.setLayout(new GridLayout(2, 1));
status.addActionListener(this);
refresh.addActionListener(this);
add.addActionListener(this);
update.addActionListener(this);
delete.addActionListener(this);
exit.addActionListener(this);
statusDisp = new StatusDisp(tcpC);
listPanel = new JPanel();
listPanel.setLayout(borderManager);
table = new JTable(pData, columnNames);
table.setEnabled(false);
scrollpane = JTable.createScrollPaneForTable(table);
c.add(scrollpane);
addWindowListener(new WindowHandler(this));
setSize(900, 700);
setLocation(420, 20);
show();
}
private void connect() {
// connect to server
try {
System.out.println("Connecting to server " + server + " " + port);
socket = new Socket(server, port);
statusDisp.display.setText("Connected with server "
+ socket.getInetAddress() + ":" + socket.getPort());
} catch (UnknownHostException e) {
statusDisp.display.setText("" + e);
System.exit(ERROR);
} catch (IOException e) {
statusDisp.display.setText("\n" + e);
System.out.println("\n" + e);
System.exit(ERROR);
}
}
private void sendData(String str) {
output.println(str);
}
public void sysPrint(String str) {
if (myDebug) {
System.out.println(str);
}
}
public String[][] getPData() {
return pData;
}
public int getEntries() {
return numEntries;
}
public void setEntries(int ent) {
numEntries = ent;
}
public int getNumEntries() {
return numEntries;
}
private void run() {
try {
input = new BufferedReader(new InputStreamReader(
socket.getInputStream()));
output = new PrintWriter(socket.getOutputStream(), true);
enterBox.requestFocus();
while (true) {
statusDisp.display
.append("\nThe names in the issue tracker records are: ");
sendData("LISTALL NAMES");
String message = input.readLine();
while (!message.toUpperCase().equals("FROM SERVER==> QUIT")) {
tokens = new StringTokenizer(message, ";");
if (tokens.countTokens() >= 1) {
int ii = 0;
while (tokens.hasMoreTokens()) {
messageTokens[ii] = tokens.nextToken().toString();
ii++;
}
if (messageTokens[0].toUpperCase()
.equals("RECORDFOUND")) {
showName();
} else if (messageTokens[0].toUpperCase().equals(
"LISTRECORD")) {
listNames();
} else if (messageTokens[0].toUpperCase().equals(
"LISTALL")) {
sysPrint("\nProcessing ListAll data.");
message = (String) input.readLine();
iii = 0;
while (!message.equals("GetAllDone;;")) {
tokens = new StringTokenizer(message, ";;");
pData[iii][0] = tokens.nextToken().toString()
.trim();
sysPrint("\nThe value of pData [iii][ 0 ] is: "
+ pData[iii][0]);
ii = 1;
while (tokens.hasMoreTokens() && ii < 11) {
pData[iii][ii] = tokens.nextToken()
.toString();
ii++;
}
sysPrint("\n" + message);
message = (String) input.readLine();
statusDisp.display.append("\nListAll record "
+ pData[iii][0] + " " + pData[iii][1]
+ " " + pData[iii][2] + " "
+ pData[iii][3] + " " + pData[iii][4]
+ " " + pData[iii][5] + " "
+ pData[iii][6] + " " + pData[iii][7]
+ " " + pData[iii][8]);
iii++;
listAllNames();
}
numEntries = iii - 1;
sysPrint("\n" + message);
initPass = false;
table = new JTable(pData, columnNames);
this.repaint();
} else if (messageTokens[0].toUpperCase().equals(
"RECORDDELETED")) {
statusDisp.display.append("\n" + message);
} else if (messageTokens[0].toUpperCase().equals(
"NOTFOUND")) {
statusDisp.display.append("\n" + message);
}
}
message = input.readLine();
}
}
} catch (IOException e) {
statusDisp.display.append("\n" + e);
}
}
public void actionPerformed(ActionEvent e) {
if (e.getSource() == exit) {
closeConnection();
}
else if (e.getSource() == refresh) {
table = new JTable(pData, columnNames);
table.repaint();
} else if (e.getSource() == clear) {
enterBox.setText("");
}
else if (e.getSource() == status) {
statusDisp.setVisible(true);
} else if (e.getSource() == add) {
JOptionPane.showMessageDialog(null,
"1: Enter a unique Record ID. \n"
+ "2: Technician Name. \n" + "3: Enter \n"
+ "- Customer Name \n" + "- Issue Details \n"
+ "- Urgency \n" + "- Status \n"
+ "4: Then press enter.\n", "Add Record",
JOptionPane.INFORMATION_MESSAGE);
newRec = new NewRec(tcpC, table, pData);
newRec.setVisible(true);
} else if (e.getSource() == update) {
JOptionPane.showMessageDialog(null,
"1: Enter the Record ID that you want to update. "
+ "\n2: Then press enter.", "Update Record",
JOptionPane.INFORMATION_MESSAGE);
updateRec = new UpdateRec(tcpC, pData, iii);
} else if (e.getSource() == delete) {
deleteRec = new DeleteRec(tcpC, table, pData);
deleteRec.show(true);
}
}
// Show name found - from server
private void showName() {
statusDisp.display.append("\n Name: " + messageTokens[1] + " "
+ messageTokens[2]);
statusDisp.display.append("\n : " + messageTokens[3]);
statusDisp.display.append("\n : " + messageTokens[4]);
}
private void listNames() {
if (!messageTokens[1].equals("") && !messageTokens[1].equals(" ")) {
statusDisp.display.append("\n Name: " + messageTokens[1]);
}
}
private void listAllNames() {
}
// Closing the Socket to the server
private void closeConnection() {
sendData("QUIT");
try {
socket.close();
input.close();
output.close();
} catch (IOException e) {
statusDisp.display.append("\n" + e);
}
setVisible(false);
System.exit(0);
}
public void paintComponent(Graphics g) {
super.paintComponents(g);
this.remove(table);
table = new JTable(pData, columnNames);
this.add(table);
}
// Entry point - JVM Calls
public static void main(String[] args) {
final Client client = new Client();
client.tcpC = client;
client.addWindowListener(new WindowAdapter() {
public void windowClosing(WindowEvent e) {
client.closeConnection();
}
});
}
// Closing the socket connect when application finishes
class WindowHandler extends WindowAdapter {
Client tcpC;
public WindowHandler(Client t) {
tcpC = t;
}
public void windowClosing(WindowEvent e) {
tcpC.closeConnection();
}
}
class StatusDisp extends Dialog implements ActionListener {
private JButton exit;
private int theRecID, ii;
private String pData[][];
private Client tclient;
JTextArea display;
// Status Display constructor
public StatusDisp(Client t_client) {
super(new Frame(), "Status Display", true);
tclient = t_client;
setup();
}
// Initialise the application
public void setup() {
display = new JTextArea();
display.setEditable(false);
add(new JScrollPane(display), BorderLayout.CENTER);
setSize(400, 280);
exit = new JButton("Exit");
exit.addActionListener(this);
add(exit, BorderLayout.SOUTH);
}
public void actionPerformed(ActionEvent e) {
if (e.getSource() == exit) {
clear();
}
}
// Clears the status display
private void clear() {
setVisible(false);
}
}
class UpdateRec extends Dialog implements ActionListener {
private JTextField recID, TechName, CustName, IssueDetails, Urgency,
Status;
private JLabel recIDLabel, TechNameLabel, CustNameLabel,
IssueDetailsLabel, UrgencyLabel, StatusLabel;
private JButton cancel, save;
private int theRecID, ii, toCont;
private String pData[][];
private Client tclient;
// Update records constructor
public UpdateRec(Client t_client, String p_Data[][], int iiPassed) {
super(new Frame(), "Update Record", true);
pData = p_Data;
ii = iiPassed;
tclient = t_client;
setup();
setVisible(true);
}
// Initializing the application
public void setup() {
setSize(400, 280);
setLayout(new GridLayout(12, 2));
setLayout(new GridLayout(14, 2));
recID = new JTextField(10);
TechName = new JTextField(10);
CustName = new JTextField(10);
IssueDetails = new JTextField(10);
Urgency = new JTextField(10);
Status = new JTextField(10);
recIDLabel = new JLabel("Record ID");
TechNameLabel = new JLabel("Technician Name");
CustNameLabel = new JLabel("Customer Name");
IssueDetailsLabel = new JLabel("Issue Details");
UrgencyLabel = new JLabel("Urgency");
StatusLabel = new JLabel("Status");
save = new JButton("Save Changes");
cancel = new JButton("Cancel");
recID.addActionListener(this);
save.addActionListener(this);
cancel.addActionListener(this);
add(recIDLabel);
add(recID);
add(TechNameLabel);
add(TechName);
add(CustNameLabel);
add(CustName);
add(IssueDetailsLabel);
add(IssueDetails);
add(UrgencyLabel);
add(Urgency);
add(StatusLabel);
add(Status);
add(save);
add(cancel);
}
// Data entry method
public void actionPerformed(ActionEvent e) {
if (e.getSource() == recID) {
theRecID = Integer.parseInt(recID.getText());
sysPrint("The value of theRecID is " + theRecID);
if (theRecID > 0) {
for (int i = 0; i <= getNumEntries(); i++) {
if (!(pData[i][0] == null)) {
sysPrint("The value of pData[ i ] [ 0 ] is "
+ Integer.parseInt(pData[i][0]));
} else {
sysPrint("The value of pData[ i ] [ 0 ] is "
+ pData[i][0]);
}
if (!(pData[i][0]).equals("")
&& Integer.parseInt(pData[i][0]) == theRecID) {
theRecID = i;
break;
}
}
recID.setText(pData[theRecID][0]);
TechName.setText(pData[theRecID][1]);
CustName.setText(pData[theRecID][2]);
IssueDetails.setText(pData[theRecID][3]);
Urgency.setText(pData[theRecID][4]);
Status.setText(pData[theRecID][5]);
} else
recID.setText("This record " + theRecID + " does not exist");
} else if (e.getSource() == save) {
pData[theRecID][0] = recID.getText();
pData[theRecID][1] = TechName.getText().trim();
pData[theRecID][2] = CustName.getText().trim();
pData[theRecID][3] = IssueDetails.getText().trim();
pData[theRecID][4] = Urgency.getText().trim();
pData[theRecID][5] = Status.getText();
for (int iii = 0; iii < pData.length; iii++) {
if ((pData[iii][0]).equals(recID.getText())) {
theRecID = iii;
break;
}
}
table = new JTable(pData, columnNames);
table.repaint();
sendData("Update;; " + pData[theRecID][0] + ";; "
+ pData[theRecID][1] + ";; " + pData[theRecID][2]
+ ";; " + pData[theRecID][3] + ";; "
+ pData[theRecID][4] + ";; " + pData[theRecID][5]
+ ";; ");
toCont = JOptionPane.showConfirmDialog(null,
"Do you want to add another record? \nChoose one",
"Choose one", JOptionPane.YES_NO_OPTION);
if (toCont == JOptionPane.YES_OPTION) {
recID.setText("");
TechName.setText("");
CustName.setText("");
IssueDetails.setText("");
Urgency.setText("");
Status.setText("");
} else {
clear();
}
} else if (e.getSource() == cancel) {
clear();
}
}
// Resets the fields
private void clear() {
recID.setText("");
TechName.setText("");
CustName.setText("");
IssueDetails.setText("");
Urgency.setText("");
Status.setText("");
setVisible(false);
}
}
class NewRec extends Dialog implements ActionListener {
private JTextField recID, TechName, CustName, IssueDetails, Urgency,
Status;
private JLabel recIDLabel, TechNameLabel, CustNameLabel,
IssueDetailsLabel, UrgencyLabel, StatusLabel;
private JButton cancel, save;
private int recIDNum, toCont;
private JTable table;
private JPanel addressPanel;
private String pData[][];
private boolean recExists = false;
private Client tclient;
// NewRec constructor
public NewRec(Client t_client, JTable tab, String p_Data[][]) {
super(new Frame(), "New Record", true);
table = tab;
pData = p_Data;
tclient = t_client;
setup();
setSize(400, 250);
}
// Initializing the application
public void setup() {
setLayout(new GridLayout(14, 2));
recID = new JTextField(10);
recID.setEnabled(false);
TechName = new JTextField(10);
CustName = new JTextField(10);
IssueDetails = new JTextField(10);
Urgency = new JTextField(10);
Status = new JTextField(10);
recIDLabel = new JLabel("Record ID");
TechNameLabel = new JLabel("Technician Name");
CustNameLabel = new JLabel("Customer Name");
IssueDetailsLabel = new JLabel("Issue Details");
UrgencyLabel = new JLabel("Urgency Level (1-5)");
StatusLabel = new JLabel("Status (SOLVED/UNSOLVED)");
save = new JButton("Save Changes");
cancel = new JButton("Cancel");
recID.addActionListener(this);
save.addActionListener(this);
cancel.addActionListener(this);
add(recIDLabel);
add(recID);
add(TechNameLabel);
add(TechName);
add(CustNameLabel);
add(CustName);
add(IssueDetailsLabel);
add(IssueDetails);
add(UrgencyLabel);
add(Urgency);
add(StatusLabel);
add(Status);
add(save);
add(cancel);
}
// Cancel/Save button action listner
public void actionPerformed(ActionEvent e) {
if (e.getSource() == cancel) {
clear();
} else if (e.getSource() == recID) {
if (recID.getText().equals(null) || recIDNum <= 0
|| recIDNum > 300) {
JOptionPane
.showMessageDialog(
null,
"A Record ID entered was: null or blank, or not between 0 and 300. which is invalid.\n"
+ "Please enter a number greater than 0 and less than 300.",
"RecID Entered", JOptionPane.ERROR_MESSAGE);
} else {
for (int i = 0; i <= getNumEntries(); i++) {
if (Integer.parseInt(pData[i][0]) == recIDNum) {
recIDNum = i;
recExists = true;
break;
}
}
if (recExists) {
JOptionPane.showMessageDialog(null, "A recID entered "
+ recID.getText() + " already exists.",
"RecID Exists", JOptionPane.ERROR_MESSAGE);
recExists = false;
}
}
} else if (e.getSource() == save) {
sysPrint("\n1a: Currrently in add() class actionPerformed() method and save construct.");
if ((TechName.getText() != "") && (CustName.getText() != "")) {
sysPrint("\n1b: Currrently in add() class - checking for duplicate recID.");
sysPrint("\n1d: Currrently in add() class actionPerformed() method - getting data for add.");
recIDNum = getNumEntries() + 1;
pData[recIDNum][0] = recID.getText();
sysPrint("A new record is being added at "
+ pData[recIDNum][0]);
pData[recIDNum][1] = TechName.getText().trim();
pData[recIDNum][2] = CustName.getText().trim();
pData[recIDNum][3] = IssueDetails.getText().trim();
pData[recIDNum][4] = Urgency.getText().trim();
pData[recIDNum][5] = Status.getText().trim();
table = new JTable(pData, columnNames);
this.repaint();
setEntries(getEntries() + 1);
sendData("Add;; " // + pData[ recIDNum ] [ 0 ] + ";; "
+ pData[recIDNum][1] + ";; "
+ pData[recIDNum][2]
+ ";; " + pData[recIDNum][3]
+ ";; "
+ pData[recIDNum][4] + ";; "
+ pData[recIDNum][5] + ";;;");
toCont = JOptionPane.showConfirmDialog(null,
"Do you want to add another record? \nChoose one",
"Choose one", JOptionPane.YES_NO_OPTION);
if (toCont == JOptionPane.YES_OPTION) {
recID.setText("");
TechName.setText("");
CustName.setText("");
IssueDetails.setText("");
Urgency.setText("");
Status.setText("");
} else {
clear();
}
}
}
}
// Clears the NewRec dialog
private void clear() {
sysPrint("\n1e: Currrently in add() class clear() method.");
setVisible(false);
}
}
class DeleteRec extends Dialog implements ActionListener {
private JTextField recID;
private JLabel recIDLabel;
private JButton cancel, delete;
// private Record data;
private int partNum, iii = 0;
private int theRecID = -1, toCont;
private JTable table;
private String pData[][];
private Client tclient;
// DeleteRec constructor
public DeleteRec(Client t_client, JTable tab, String p_Data[][]) {
super(new Frame(), "Delete Record", true);
setSize(400, 150);
setLayout(new GridLayout(2, 2));
table = tab;
pData = p_Data;
tclient = t_client;
recIDLabel = new JLabel("Record ID");
recID = new JTextField(10);
delete = new JButton("Delete Record");
cancel = new JButton("Cancel");
cancel.addActionListener(this);
delete.addActionListener(this);
recID.addActionListener(this);
add(recIDLabel);
add(recID);
add(delete);
add(cancel);
}
// Cancel/Save button action
public void actionPerformed(ActionEvent e) {
if (e.getSource() == recID) {
theRecID = Integer.parseInt(recID.getText());
} else if (e.getSource() == delete) {
theRecID = Integer.parseInt(recID.getText());
sysPrint(" The record id to be deleted is " + theRecID);
sendData("Delete;; " + (theRecID) + ";; ");
setEntries(getEntries() - 1);
for (int iii = 0; iii < pData.length; iii++) {
if ((pData[iii][0]).equals(recID.getText())) {
theRecID = iii;
break;
}
}
pData[theRecID][0] = Integer.toString(theRecID);
pData[theRecID][1] = "Deleted";
pData[theRecID][2] = " ";
pData[theRecID][3] = " ";
pData[theRecID][4] = " ";
pData[theRecID][5] = " ";
table = new JTable(pData, columnNames);
table.repaint();
toCont = JOptionPane.showConfirmDialog(null,
"Do you want to add another record? \nChoose one",
"Choose one", JOptionPane.YES_NO_OPTION);
if (toCont == JOptionPane.YES_OPTION) {
recID.setText("");
} else {
clear();
}
} else if (e.getSource() == cancel) {
clear();
}
}
// Method makes the DeleteRec dialogue invisible
private void clear() {
recID.setText("");
setVisible(false);
}
}
}
