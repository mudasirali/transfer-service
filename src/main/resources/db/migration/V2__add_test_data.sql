insert into transactions.accounts (id, title, branch_id, account_number, account_balance) values
    (1, 'John Doe', 201, 123456, 400),
    (2, 'Jane Doe', 202, 123457, 1100),
    (3, 'Johny Doe', 203, 123458, 1200),
    (4, 'Jimmy Doe', 204, 123459, 1300);


insert into transactions.transfers (sender_id, receiver_id, amount, status) values
    (1, 2, 100, 1),
    (1, 3, 200, 1),
    (1, 4, 300, 1);