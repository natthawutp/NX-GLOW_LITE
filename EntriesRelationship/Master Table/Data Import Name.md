# GWH_TM_DIN
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
| 18 | DIN_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DIN_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DIN_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DIN_DT_KND | CHAR(2) | N | Data Kind Code / データ種別コード |
| 22 | DIN_FLD_COD | NVARCHAR2(100) | N | Field ID / フィールドID |
| 23 | DIN_FLD_NAM | NVARCHAR2(400) | N | Field Name / フィールド名 |
| 24 | DIN_DT_FMT_COD | NVARCHAR2(4) | N | Data Format Code / データ型コード |
| 25 | DIN_MX_DGT | NUMBER(9) | Y | Max Digit / 最大桁数 |
| 26 | DIN_HDDTL_KND | CHAR(2) | N | Header Detail Kind / ヘッダ明細区分 |
| 27 | DIN_DT_PSTN | NUMBER(6) | N | Data Position / データ位置 |
| 28 | DIN_DT_SZ | NUMBER(3) | Y | Data Size / データサイズ |
| 29 | DIN_STMR_FLG | CHAR(1) | N | System Required Flag / システム必須フラグ |
| 30 | DIN_RQR_FLG | CHAR(1) | N | Required Flag / 必須フラグ |
| 31 | DIN_FXD_VL | NVARCHAR2(400) | Y | Fixed Value / 固定値 |
| 32 | DIN_DT_CMBN_SPLT_FLG | CHAR(1) | N | Data Combine split Flag / データ結合分割フラグ |
| 33 | DIN_DT_CNV_KND | CHAR(2) | N | Data Conversion Kind / データ変換区分 |
| 34 | DIN_DCML_DGT | NUMBER(1) | N | Decimal Digit / 小数桁数 |
| 35 | DIN_DT_FMT_BCNV | NVARCHAR2(40) | Y | Date Format Before Conversion / 日付形式_変換前 |
| 36 | DIN_DT_FMT_ACNV | NVARCHAR2(40) | Y | Date Format After Conversion / 日付形式_変換後 |
| 37 | DIN_PRH_CHR_CHK_FLG | CHAR(1) | N | Prohibit Character Check Flag / 禁則文字チェックフラグ |
| 38 | DIN_KEY_CLMN_FLG | CHAR(1) | N | Key Column Flag / キー項目フラグ |
| 39 | DIN_DSP_ORD | NUMBER(3) | N | Display Order / 表示順 |
