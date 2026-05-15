# GWH_TJ_DV_D
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
| 12 | UPD_TMID | NVARCHAR2(40) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | DVD_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DVD_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DVD_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DVD_DLCT_NUM | CHAR(14) | N | Delivery Control Number / 配送管理番号 |
| 22 | DVD_DLLN_NUM | NUMBER(4) | N | Delivery Control Line Number / 配送管理番号ラインNo |
| 23 | DVD_PROD_COD | NVARCHAR2(100) | N | Product Code / 商品コード |
| 24 | DVD_PROD_NAM | NVARCHAR2(200) | N | Product Name / 商品名称 |
| 25 | DVD_ORGN_COD | CHAR(2) | Y | Country Of Origin / 原産国 |
| 26 | DVD_SPPR_COD | NVARCHAR2(100) | Y | Supplier Product Code / サプライヤ商品コード |
| 27 | DVD_SPPR_NAM | NVARCHAR2(200) | Y | Supplier Product Name / サプライヤ商品名称 |
| 28 | DVD_PPCS_QTY | NUMBER(9) | N | Number Of Pieces Per Case / ケース入数 |
| 29 | DVD_SCS_QTY | NUMBER(9) | N | Case QTY(Sched.) / ケース予定数 |
| 30 | DVD_SPC_QTY | NUMBER(12,3) | N | Piece QTY(Sched.) / バラ予定数 |
| 31 | DVD_STPC_QTY | NUMBER(12,3) | N | Total Piece QTY(Sched.) / 総バラ予定数 |
| 32 | DVD_PCUM_PCS | NVARCHAR2(6) | Y | UOM(Piece) / 単位(バラ数) |
| 33 | DVD_PCUM_CS | NVARCHAR2(6) | Y | UOM(Case) / 単位(ケース数) |
| 34 | DVD_WGT | NUMBER(9,3) | N | Weight / 重量 |
| 35 | DVD_M3 | NUMBER(9,3) | N | Volume / 容積 |
| 36 | DVD_SBIV_COD | NVARCHAR2(40) | Y | Sub Inventry / 等級コード |
| 37 | DVD_PIK1 | NVARCHAR2(60) | Y | PICKING KEY1 / ピッキングキー1 |
| 38 | DVD_PIK2 | NVARCHAR2(60) | Y | PICKING KEY2 / ピッキングキー2 |
| 39 | DVD_PIK3 | NVARCHAR2(60) | Y | PICKING KEY3 / ピッキングキー3 |
| 40 | DVD_PIK4 | NVARCHAR2(60) | Y | PICKING KEY4 / ピッキングキー4 |
| 41 | DVD_PIK5 | NVARCHAR2(60) | Y | PICKING KEY5 / ピッキングキー5 |
| 42 | DVD_PIK6 | NVARCHAR2(60) | Y | PICKING KEY6 / ピッキングキー6 |
| 43 | DVD_PIK7 | NVARCHAR2(60) | Y | PICKING KEY7 / ピッキングキー7 |
| 44 | DVD_DMG_FLG | CHAR(1) | Y | Damage/Hold Flag / ダメージ/ホールドフラグ |
| 45 | DVD_DLV_RMKS | NVARCHAR2(1000) | Y | Remarks for Delivery Note / 送り状備考 |
| 46 | DVD_SOD_RMKS | NVARCHAR2(1000) | Y | Remarks for Statement of Delivery / 納品書備考 |
| 47 | DVD_CIT1 | NVARCHAR2(100) | Y | Customized Item 01 / 顧客専用項目01フラグ |
| 48 | DVD_CIT2 | NVARCHAR2(100) | Y | Customized Item 02 / 顧客専用項目02フラグ |
| 49 | DVD_CIT3 | NVARCHAR2(100) | Y | Customized Item 03 / 顧客専用項目03フラグ |
| 50 | DVD_CIT4 | NVARCHAR2(100) | Y | Customized Item 04 / 顧客専用項目04フラグ |
| 51 | DVD_CIT5 | NVARCHAR2(100) | Y | Customized Item 05 / 顧客専用項目05フラグ |
| 52 | DVD_CIT6 | NVARCHAR2(100) | Y | Customized Item 06 / 顧客専用項目06フラグ |
| 53 | DVD_CIT7 | NVARCHAR2(100) | Y | Customized Item 07 / 顧客専用項目07フラグ |
| 54 | DVD_CIT8 | NVARCHAR2(100) | Y | Customized Item 08 / 顧客専用項目08フラグ |
| 55 | DVD_CIT9 | NVARCHAR2(100) | Y | Customized Item 09 / 顧客専用項目09フラグ |
| 56 | DVD_CIT10 | NVARCHAR2(100) | Y | Customized Item 10 / 顧客専用項目10フラグ |
| 57 | DVD_CIT11 | NVARCHAR2(100) | Y | Customized Item 11 / 顧客専用項目11フラグ |
| 58 | DVD_CIT12 | NVARCHAR2(100) | Y | Customized Item 12 / 顧客専用項目12フラグ |
| 59 | DVD_CIT13 | NVARCHAR2(100) | Y | Customized Item 13 / 顧客専用項目13フラグ |
| 60 | DVD_CIT14 | NVARCHAR2(100) | Y | Customized Item 14 / 顧客専用項目14フラグ |
| 61 | DVD_CIT15 | NVARCHAR2(100) | Y | Customized Item 15 / 顧客専用項目15フラグ |
