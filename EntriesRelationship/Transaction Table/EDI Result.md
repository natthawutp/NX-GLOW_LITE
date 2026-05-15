# GWH_TJ_ER
#Entity #Standard #EDI

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
| 18 | ER_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | ER_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | ER_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | ER_SR_NUM | NVARCHAR2(60) | N | Send/Receive Number / 送受信NO |
| 22 | ER_SRSQ_NUM | NUMBER(4) | N | Send/Receive Processing Seq / 処理シーケンス |
| 23 | ER_SR_YMD | CHAR(8) | N | Send/Receive Date / 送受信日 |
| 24 | ER_SR_TIM | CHAR(6) | Y | Send/Receive Time / 送受信時間 |
| 25 | ER_DTKD_COD | CHAR(2) | N | Date Kind Code / データ種別コード |
| 26 | ER_SR_KND | CHAR(1) | N | Send/Receive Kind / 送受信区分 |
| 27 | ER_SR_QTY | NUMBER(9) | N | Number of Snd/Rcv records / 送受信件数（伝票明細件数） |
| 28 | ER_IMP_QTY | NUMBER(9) | N | Number of import records / 取込件数（伝票明細件数） |
| 29 | ER_ERR_QTY | NUMBER(9) | N | Number of error records / 取込エラー件数（伝票明細件数） |
| 30 | ER_DUP_QTY | NUMBER(9) | N | Number of duplicate records / 重複件数（伝票明細件数） |
| 31 | ER_DLT_QTY | NUMBER(9) | N | Number of deleted records / 削除件数（伝票明細件数） |
| 32 | ER_PRST_KND | CHAR(2) | Y | Processing Status Kind / 処理状況区分 |
| 33 | ER_FILE_NAM | NVARCHAR2(800) | N | File Name / ファイル名 |
| 34 | EE_INV_NUM_LST | NVARCHAR2(4000) | Y | Invoice Number List / 伝票番号一覧 |
| 35 | ER_IMP_INV_QTY | NUMBER(9) | Y | Number of import invoice records / 取込件数（伝票件数） |
| 36 | ER_ERR_INV_QTY | NUMBER(9) | Y | Number of error invoice records / 取込エラー件数（伝票件数） |
