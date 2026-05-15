# GWH_TJ_LD_S
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
| 18 | LDS_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | LDS_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | LDS_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | LDS_LD_NUM | NVARCHAR2(12) | N | Loading Number / 積載番号 |
| 22 | LDS_LDLN_NUM | NUMBER(4) | N | Loading Line Number / 積載ラインNo |
| 23 | LDS_LDSQ_NUM | NUMBER(4) | N | Loading Seq Number / 積載SEQ No |
| 24 | LDS_BTGP_NUM | NVARCHAR2(12) | N | Batch Grouping Number / バッチグループ番号 |
| 25 | LDS_CT_NUM | NVARCHAR2(60) | N | Car/Contianer No. / 車番/コンテナ番号 |
| 26 | LDS_CTID_NUM | NVARCHAR2(40) | Y | Loading Carton ID / カートンID |
| 27 | LDS_PROD_COD | NVARCHAR2(100) | Y | Product Code / 商品コード |
| 28 | LDS_PROD_NAM | NVARCHAR2(200) | Y | Product Name / 商品名称 |
| 29 | LDS_LD_QTY | NUMBER(12,3) | N | Loaded Qty / 積込数 |
| 30 | LDS_UOM_PCS | NVARCHAR2(6) | Y | UOM / 単位 |
| 31 | LDS_WGT | NUMBER(12,6) | N | Weight / 重量 |
| 32 | LDS_M3 | NUMBER(12,6) | N | Volume / 容積 |
| 33 | LDS_CLI1 | NVARCHAR2(100) | Y | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| 34 | LDS_CLI2 | NVARCHAR2(100) | Y | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| 35 | LDS_CLI3 | NVARCHAR2(100) | Y | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| 36 | LDS_CLI4 | NVARCHAR2(100) | Y | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| 37 | LDS_CLI5 | NVARCHAR2(100) | Y | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| 38 | LDS_CLI6 | NVARCHAR2(100) | Y | Customized List Items for printing 06 / 顧客専用リスト印字用項目06 |
| 39 | LDS_CLI7 | NVARCHAR2(100) | Y | Customized List Items for printing 07 / 顧客専用リスト印字用項目07 |
| 40 | LDS_CLI8 | NVARCHAR2(100) | Y | Customized List Items for printing 08 / 顧客専用リスト印字用項目08 |
| 41 | LDS_CLI9 | NVARCHAR2(100) | Y | Customized List Items for printing 09 / 顧客専用リスト印字用項目09 |
| 42 | LDS_ITM_KND | CHAR(2) | N | Loading Item kind / 積込商品区分 |
| 43 | LDS_RMKS | NVARCHAR2(1000) | Y | Remarks / 備考 |
