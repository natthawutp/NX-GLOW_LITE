# GWH_TM_DIC
#Entity #Standard #MASTER

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
| 18 | DIC_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DIC_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DIC_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DIC_DT_KND | CHAR(2) | N | Data Kind Code / データ種別コード |
| 22 | DIC_DT_KND_NAM | NVARCHAR2(400) | N | Data Kind Name / データ種別名称 |
| 23 | DIC_FL_FMT | CHAR(3) | N | File Format / ファイル形式 |
| 24 | DIC_DLIM_KND | CHAR(2) | Y | Delimiter / 区切り文字区分 |
| 25 | DIC_CHR_ENC | CHAR(3) | N | Character Encoding / 文字コード |
| 26 | DIC_NEW_LIN_CHR | CHAR(2) | N | New Line Character / 改行コード |
| 27 | DIC_DT_STLN_NUM | NUMBER(3) | Y | Data Start Line Number / データ開始行番号 |
| 28 | DIC_HD_QNT_LNG | NUMBER(5) | Y | Header Quantity Length / ヘッダ項目数・レコード長 |
| 29 | DIC_DTL_QNT_LNG | NUMBER(5) | Y | Detail Quantity Length / 明細項目数・レコード長 |
| 30 | DIC_FL_KND | CHAR(2) | N | File Kind / ファイル区分 |
| 31 | DIC_FL_KND_PSTN | NUMBER(3) | Y | File Kind Position / ファイル区分_位置 |
| 32 | DIC_FL_KND_SZ | NUMBER(5) | Y | File Kind Size / ファイル区分_サイズ |
| 33 | DIC_FL_KND_HD | NVARCHAR2(8) | Y | File Kind Header / ファイル区分_ヘッダ |
| 34 | DIC_FL_KND_DTL | NVARCHAR2(8) | Y | File Kind Detail / ファイル区分_明細 |
| 35 | DIC_DFFR_INCLD_FLG | CHAR(1) | N | Difference Including Flag / 差分取込フラグ |
