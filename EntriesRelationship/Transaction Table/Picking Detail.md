# GWH_TJ_SPK_D
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
| 18 | SPKD_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | SPKD_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | SPKD_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | SPKD_PKGP_NO | NVARCHAR2(16) | N | Picking Group No. |
| 22 | SPKD_AS_NUM | NVARCHAR2(12) | N | Arrival and Shipping Number |
| 23 | SPKD_ASLN_NUM | NUMBER(4) | N | Arrival and Shipping Line Number |
| 24 | SPKD_ASSQ_NUM | NUMBER(4) | N | Arrival and Shipping Seq Number |
| 25 | SPKD_QC_RCS_QTY | NUMBER(9) | Y | QC Total Case QTY |
| 26 | SPKD_QC_RBL_QTY | NUMBER(9) | Y | QC Total Ball QTY |
| 27 | SPKD_QC_RPC_QTY | NUMBER(12,3) | Y | QC Total Piece QTY |
| 28 | SPKD_QC_RSN_RMKS | NVARCHAR2(100) | Y | QC Reason |
| 29 | SPKD_QC_PASS_FLG | CHAR(1) | N | QC Passed Flag |
| 30 | SPKD_PIK_RCS_QTY | NUMBER(9) | Y | Pick Case QTY |
| 31 | SPKD_PIK_RBL_QTY | NUMBER(9) | Y | Pick Ball QTY |
| 32 | SPKD_PIK_RPC_QTY | NUMBER(12,3) | Y | Pick Piece QTY |
| 33 | SPKD_NUM_SCN | NUMBER(9) | Y | Number of Scan |
| 34 | SPKD_ST_PASS_FLG | CHAR(1) | N | Sorting Passed Flag |
| 35 | SPKD_BIN_NUM | NVARCHAR2(60) | Y |  |
| 36 | SPKD_PG_QTY | NUMBER(12) | Y |  |
| 37 | SPKD_TOT_PG_QTY | NUMBER(12) | Y |  |
| 38 | SPKD_BIN_STS | NVARCHAR2(4) | Y |  |
| 39 | SPKD_PROD_COD | NVARCHAR2(100) | Y |  |
