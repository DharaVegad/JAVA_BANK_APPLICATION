create schema bankingproject;
use bankingproject;

-- select * from Customers;
-- select * from Login;

CREATE TABLE Customers(
ID int PRIMARY KEY auto_increment NOT NULL,
firstname varchar(30) NOT  NULL,
lastname varchar(30) NOT  NULL,
email varchar(100) NOT  NULL
);

CREATE TABLE Login(
CustomerID int primary key NOT NULL,
username varchar(100) NOT NULL unique,
password varchar(255) NOT NULL,
FOREIGN KEY(CustomerID) REFERENCES Customers(ID) on delete cascade
);

CREATE TABLE Accounts(
ID int primary key auto_increment NOT NULL,
CustomerID int,
accountType varchar(20) NOT NULL,
balance decimal(10,3)NOT NULL DEFAULT 0 CHECK(balance>=0),
isActive boolean DEFAULT TRUE,
FOREIGN KEY(CustomerID) REFERENCES Customers(ID) on delete cascade
);

ALTER TABLE Accounts AUTO_INCREMENT=10000;

CREATE TABLE TransactionCategories(
ID int primary KEY auto_increment NOT NULL,
CategoryName varchar(255) NOT NULL unique
);

CREATE TABLE TransactionCategoryOptions(
ID int primary KEY auto_increment NOT NULL,
categoryID int NOT NULL,
optionTitle varchar(100) NOT NULL,
inputName varchar(100) NOT NULL,
inputType varchar(30) NOT NULL DEFAULT 'text',
FOREIGN KEY(categoryID)REFERENCES TransactionCategories(ID)
);

CREATE TABLE Transaction(
ID int primary key NOT NULL,
CustomerID int NOT NULL,
fromAccountId int NOT NULL,
commitDate timestamp not null default current_timestamp.
amount decimal(10,2) NOT NULL,
remark varchar(100) DEFAULT '',
status ENHUM('completed','failed','incomplete','deposited')default 'incomplete',
FOREIGN KEY(CustomerID)REFERENCES Customers(ID),
FOREIGN KEY(fromAccountId) REFERENCES Accounts(ID)
);

CREATE TABLE TransactionValues(
ID int primary key auto_increment NOT NULL,
optionID int NOT NULL,
optionValue varchar(100) NOT NULL,
transtioniD int NOT NULL,
FOREIGN KEY(transtioniD)REFERENCES Transaction(ID) ON Delete CASCADE,
FOREIGN KEY(optionID) REFERENCES TransactionCategoryOptions(ID)
);

CREATE TABLE WithinBankTransactions(
transtioniD int NOT NULL,
toAccountID int NOT NULL,
PRIMARY KEY(transtioniD),
FOREIGN KEY(transtioniD) REFERENCES Transaction(ID) ON Delete CASCADE,
FOREIGN KEY(toAccountID)REFERENCES Accounts(ID)
);

CREATE TABLE OtherTransactions(
transtioniD int,
CategoryID int,
PRIMARY KEY(transtioniD),
FOREIGN KEY(transtioniD)REFERENCES Transaction(ID)ON Delete CASCADE
);


INSERT INTO bankingproject.TransactionCategories(ID,CategoryName)Values(1,'Bank Transfer');
INSERT INTO bankingproject.TransactionCategories(ID,CategoryName)Values(2,'Mobile Bill');
INSERT INTO bankingproject.TransactionCategories(ID,CategoryName)Values(3,'Electricity Bill');
INSERT INTO bankingproject.TransactionCategories(ID,CategoryName)Values(4,'Withdraw Money');
INSERT INTO bankingproject.TransactionCategories(ID,CategoryName)Values(5,'Deposite Money');

Select *from bankingproject.TransactionCategories;

INSERT INTO bankingproject.TransactionCategoriesOptions(CategoryID,optionTitle,inputName,inputType)Values(1,'Account Number','account_number','text');
INSERT INTO bankingproject.TransactionCategoriesOptions(CategoryID,optionTitle,inputName,inputType)Values(2,'IFSC Code','ifsc_code','text');
INSERT INTO bankingproject.TransactionCategoriesOptions(CategoryID,optionTitle,inputName,inputType)Values(3,'Bill Number','account_number','text');
INSERT INTO bankingproject.TransactionCategoriesOptions(CategoryID,optionTitle,inputName,inputType)Values(4,'Bill Number','account_number','text');

select * from bankingproject.TransactionCategoryOptions;


INSERT INTO Customers(firstname,lastname,email)Values('Kamna','Rajput','kamnamehra4@gmail.com');
select * from bankingproject.Customers;


INSERT INTO BagnkingProject.Login(CustomerID,username,password) Values (1,'kamnamehra4@gmail.com','password');
select * from bankingproject.Login;

INSERT INTO bankingproject.accounts(CustomerID,accountType,balance)Values(1,'saving',1000.48);
INSERT INTO bankingproject.accounts(CustomerID,accountType,balance)Values(2,'credit',58.88);
Select * from Accounts;

INSERT INTO bankingproject.transaction(ID,CustomerID,fromAccountId,amount,remark) Values(1,5,10000,1000.48,'text');
select * from BagnkingProject.Transaction;


INSERT INTO bankingproject.transactionvalues(optionID,optionValue,transtioniD)Values (1,'OP12',785563)
INSERT INTO bankingproject.transactionvalues(optionID,optionValue,transtioniD)Values (1,'OP13',785563)
select * from BagnkingProject.TransactionValues tv inner join Transaction t on t.ID=tv.transtioniD inner join TransactionCategoryOptions tco.ID=tv.optionID where transtioniD=785563;

--select from Transaction where ID=785563;

INSERT INTO bankingproject.OtherTransactions(transtioniD,categoryID)Values(785563,2);

select * from BagnkingProject.OtherTransactions ot 
inner join bankingproject.Transaction t on t.ID=ot.transtioniD
inner join BagnkingProject.TransactionCategories tg on tg.ID=ot.categoryID
inner join BagnkingProject.TransactionValues tv=t.ID=tv.transtioniD