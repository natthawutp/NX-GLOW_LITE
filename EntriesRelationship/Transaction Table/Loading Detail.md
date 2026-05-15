# GWH_TJ_LD_D
#Entity #Standard #OUTBOUND

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | LDD_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | LDD_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | LDD_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | LDD_LD_NUM | NVARCHAR2(12) | N | Loading Number / 積載番号 |
| 22 | LDD_LDLN_NUM | NUMBER(4) | N | Loading Line Number / 積載ラインNo |
| 23 | LDD_BTGP_NUM | NVARCHAR2(12) | N | Batch Grouping Number / バッチグループ番号 |
| 24 | LDD_SLD_QTY | NUMBER(12,3) | N | Loaded QTY(Sched.) / 積込予定数 |
| 25 | LDD_RLD_QTY | NUMBER(12,3) | N | Loaded QTY(Result.) / 積込実績数 |
| 26 | LDD_SWGT | NUMBER(12,6) | N | Weight(Sched.) / 重量(予定) |
| 27 | LDD_SM3 | NUMBER(12,6) | N | Volume(Sched.) / 容積(予定) |
| 28 | LDD_RWGT | NUMBER(12,6) | N | Weight(Result.) / 重量(実績) |
| 29 | LDD_RM3 | NUMBER(12,6) | N | Volume(Result.) / 容積(実績) |
| 30 | LDD_CLI1 | NVARCHAR2(100) | Y | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| 31 | LDD_CLI2 | NVARCHAR2(100) | Y | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| 32 | LDD_CLI3 | NVARCHAR2(100) | Y | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| 33 | LDD_CLI4 | NVARCHAR2(100) | Y | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| 34 | LDD_CLI5 | NVARCHAR2(100) | Y | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| 35 | LDD_CLI6 | NVARCHAR2(100) | Y | Customized List Items for printing 06 / 顧客専用リスト印字用項目06 |
| 36 | LDD_CLI7 | NVARCHAR2(100) | Y | Customized List Items for printing 07 / 顧客専用リスト印字用項目07 |
| 37 | LDD_CLI8 | NVARCHAR2(100) | Y | Customized List Items for printing 08 / 顧客専用リスト印字用項目08 |
| 38 | LDD_CLI9 | NVARCHAR2(100) | Y | Customized List Items for printing 09 / 顧客専用リスト印字用項目09 |
| 39 | LDD_ITM_KND | CHAR(2) | N | Loading Item kind / 積込商品区分 |
| 40 | LDD_RMKS | NVARCHAR2(1000) | Y | Remarks / 備考 |
